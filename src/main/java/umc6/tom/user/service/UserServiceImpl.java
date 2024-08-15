package umc6.tom.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.alarm.converter.AlarmSetConverter;
import umc6.tom.alarm.repository.AlarmSetRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.*;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.board.repository.BoardLikeRepository;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.dto.CommentBoardDto;
import umc6.tom.comment.dto.LikeBoardDto;
import umc6.tom.comment.dto.PinBoardDto;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.repository.CommentRepository;
import umc6.tom.comment.repository.PinRepository;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.Uuid;
import umc6.tom.common.repository.UuidRepository;
import umc6.tom.config.AmazonConfig;
import umc6.tom.security.JwtTokenProvider;
import umc6.tom.user.converter.ResignConverter;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.ResignDtoReq;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.Prohibit;
import umc6.tom.user.model.Resign;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Agreement;
import umc6.tom.user.model.enums.SocialType;
import umc6.tom.user.model.enums.UserStatus;
import umc6.tom.user.repository.*;
import umc6.tom.util.AmazonS3Util;
import umc6.tom.util.CookieUtil;
import umc6.tom.util.RedisUtil;
import umc6.tom.util.SmsUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResignRepository resignRepository;
    private final MajorsRepository majorsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlarmSetRepository alarmSetRepository;
    private final SmsUtil smsUtil;
    private final RedisUtil redisUtil;
    private final AmazonS3Util amazonS3Util;
    private final UuidRepository uuidRepository;
    private final AmazonConfig amazonConfig;

    private static final String DEFAULT_PROFILE_PATH = "https://yesol.s3.ap-northeast-2.amazonaws.com/profile/defaultProfile.png";
    private final BoardRepository boardRepository;
    private final PinRepository pinRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;
    private final ProhibitRepository prohibitRepository;


    // 회원 가입
    @Override
    public User join(UserDtoReq.JoinDto request) {

        if (duplicatedNickName(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_DUPLICATED);
        }   // 닉네임 중복 확인
        if (duplicatedAccount(request.getAccount())) {
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_DUPLICATED);
        }   // 아이디 중복 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            log.info("이미 사용중인 휴대폰 번호 등록 관리자 확인 필요!");
            if (userRepository.findByPhone(request.getPhone()).get().getSocialType().equals(SocialType.KAKAO)) {
                throw new UserHandler(ErrorStatus.USER_IS_ALREADY_REGISTERED_KAKAO);
            }
        }

        Majors major = majorsRepository.findById(request.getMajor())
                .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

        User user = UserConverter.toUser(request.getName(), request.getNickName(), request.getAccount(),
                // 비밀번호 암호화
                passwordEncoder.encode(request.getPassword()),
                // 휴대폰 번호 "-" 제거
                request.getPhone().replaceAll("-", ""),
                SocialType.NON,
                major,
                DEFAULT_PROFILE_PATH);

        userRepository.save(user);
        prohibitRepository.save(Prohibit.builder().user(user).build());
        alarmSetRepository.save(AlarmSetConverter.convertAlarmSetToAlarmSet(user));

        return user;
    }

    // 카카오 회원가입
    @Override
    @Transactional
    public User joinKakao(UserDtoRes.kakaoJoinDto kakaoRequest, UserDtoReq.JoinDto request) {

        // 이름, 번호는 필수
        String phone = kakaoRequest.getPhone();
        String name = kakaoRequest.getName();
        String nickName = null;
        String account = request.getAccount();
        User user;

        // 이미 가입된 회원
        if (userRepository.findByPhone(phone).isPresent()) {
            // 카카오로 통합 계정 사용 - 휴대폰 번호는 유일하다. 카카오는 신뢰성 있다.
            user = userRepository.findByPhone(kakaoRequest.getPhone())
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            user.setSocialType(SocialType.KAKAO);
        }
        // 가입되어있지 않은 회원 - 가입
        else {
            // 닉네임
            if (!kakaoRequest.getNickName().isEmpty()) {
                nickName = kakaoRequest.getNickName();
                duplicatedNickName(nickName);
            }
            else {
                nickName = request.getNickName();
            }

            Majors major = majorsRepository.findById(request.getMajor())
                    .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

            user = UserConverter.toUser(name, nickName, account,
                    // 비밀번호 암호화 - 초기 비밀번호는 password
                    passwordEncoder.encode("password"),
                    phone,
                    SocialType.KAKAO,
                    major,
                    kakaoRequest.getProfilePic());

            userRepository.save(user);
        }

        return user;
    }

    // 휴대폰 인증
    @Override
    public UserDtoRes.PhoneAuthDto phoneAuth(UserDtoReq.PhoneDto request) {

        // 인증번호 생성
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(900000) + 100000;
        String verificationCode = String.format("%06d", randomNumber);

        // 휴대폰 번호 "-" 제거
        request.setPhone(request.getPhone().replaceAll("-", ""));

        log.info("{} 인증번호 : {}", request.getPhone(), verificationCode);
        smsUtil.sendMessage(request.getPhone(), verificationCode);

        redisUtil.setDataAndExpire(verificationCode, request.getPhone(), 60 * 5L);

        return UserConverter.phoneAuth(request.getPhone());
    }

    // 로그인
    @Override
    public UserDtoRes.LoginDto login(HttpServletRequest request, HttpServletResponse response, UserDtoReq.LoginDto req) {

        UserDtoRes.suspensionDto suspension = null;

        String account = req.getAccount();
        String password = req.getPassword();

        List<UserStatus> statuses = List.of(UserStatus.ACTIVE, UserStatus.INACTIVE, UserStatus.WITHDRAW, UserStatus.SUSPENSION);

        User user = userRepository.findByAccountAndStatusIn(account, statuses)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        log.info("로그인 유저 : {} {}", user.getUsername(), user.getStatus());

        // 비밀번호 불일치
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }
        log.info("비밀번호 검증 통과 user : {}", user.getUsername());

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new PhoneHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        // 탈퇴된 유저는 기간내 로그인 할 시 WITHDRAW to ACTIVE, 로그인
        if (user.getStatus() == UserStatus.WITHDRAW) {
            resignRepository.deleteByUser(user);
            user.setStatus(UserStatus.ACTIVE);
            log.info("탈퇴한 유저 {} 재가입", user.getId());
        }

        // 선 응답
        if (user.getStatus() == UserStatus.SUSPENSION) {
            Prohibit prohibit = prohibitRepository.findById(user.getId())
                    .orElseThrow(() -> new ProhibitHandler(ErrorStatus.PROHIBIT_NOT_FOUND));

            suspension = UserDtoRes.suspensionDto.builder()
                    .nickName(user.getNickName())
                    .message(prohibit.getMessage())
                    .boardId(prohibit.getBoard().getId())
                    .title(prohibit.getBoard().getTitle())
                    .content(prohibit.getBoard().getContent().substring(0, 20))
                    .build();
        }

        // 정지 시기가 지났을 시 Status 값 변경
        if (user.getStatus().equals(UserStatus.SUSPENSION)) {
            Prohibit prohibit = prohibitRepository.findById(user.getId())
                    .orElseThrow(() -> new ProhibitHandler(ErrorStatus.PROHIBIT_NOT_FOUND));
            if (prohibit.getSuspensionDue().isBefore(LocalDateTime.now())) {
                user.setStatus(UserStatus.ACTIVE);
            }
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        log.info("login refresh token : {}", refreshToken);

        // 쿠키 저장
        CookieUtil.deleteCookie(request, response, "refreshToken");
        CookieUtil.addCookie(response, "refreshToken", refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_COOKIE);
        // DB 의 refreshToken 삭제 후 저장 변경

        redisUtil.setDataAndExpire("RT:" + user.getId(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME_IN_REDIS);

        return UserConverter.signInRes(user, accessToken, suspension);
    }

    // 쿠키의 RefreshToken 으로 AccessToken 재발행 / Redis 의 refreshToken 과 비교
    @Override
    public UserDtoRes.ReissueDto reissue(String refreshToken) {

        // 토큰 유효성 검사
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID);
        }

        Long userId = jwtTokenProvider.getUserIdInToken(refreshToken);
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String refreshTokenInRedis = redisUtil.getValue("RT:" + userId);

        if (!refreshTokenInRedis.equals(refreshToken)) {
            throw new JwtHandler(ErrorStatus.JWT_REFRESHTOKEN_NOT_MATCHED);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        // 새로운 refreshToken 발급하는 로직 -> 보안 강화! (작성 예정)
        return UserConverter.reissueRes(newAccessToken, user);
    }

    // 닉네임 중복 확인 - 중복이 있다면 true
    @Override
    public boolean duplicatedNickName(String nickName) {
        return userRepository.findByNickName(nickName).isPresent();
    }

    // 아이디 중복 확인 - 중복이 있다면 true
    @Override
    public boolean duplicatedAccount(String account) {
        return userRepository.findByAccount(account).isPresent();
    }

    // 회원 탈퇴 UserStatus ACTIVE -> WITHDRAW
    @Override
    public void withDraw(Long userId, UserDtoReq.WithDrawDto request) {

        User user = findUser(userId);

        // 기존 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        // 기존 UserStatus.WITHDRAW or resign 존재시
        if (user.getStatus() == UserStatus.WITHDRAW || resignRepository.findByUser(user).isPresent()) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_WITHDRAW);
        }

        // 회원 탈퇴시 탈퇴 정보 저장, 재가입 대기 시간 24시간(밀리초)
        final long USER_WAIT_WITHDRAWAL_TIME = 1000L * 60 * 60 * 24;

        ResignDtoReq.saveDto resign = ResignDtoReq.saveDto
                .builder()
                .user(user)
                .reason(request.getReason())
                .timer(convertToLocalDateTime(USER_WAIT_WITHDRAWAL_TIME))
                .build();

        resignRepository.save(ResignConverter.toResign(resign));

        user.setStatus(UserStatus.WITHDRAW);
    }

    // 재가입대기시간 이후 Resign, User 를 삭제하는 메서드
    @Override
    public void deleteUser() {

        List<Resign> resigns = resignRepository.findByTimerBefore(LocalDateTime.now());
        List<User> users = new ArrayList<>();

        if (!resigns.isEmpty()) {
            // 삭제할 User 객체를 빼낸 후 Resign 삭제, 이후 userService 로 전달
            for (Resign resign : resigns) {
                users.add(resign.getUser());
            }
            resignRepository.deleteAll(resigns);
            log.info("Withdraw User's Resign deleted.");
            userRepository.deleteAll(users);
            log.info("Withdraw User deleted.");
        }
    }

    // 로그아웃
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        Long userId = jwtTokenProvider.getUserIdInToken(accessToken);

//        Long expiration = jwtTokenProvider.expireToken(accessToken);
        // Cookie 에 있는 RefreshToken 의 데이터를 value 0, 만료 0 으로 초기화
        CookieUtil.addCookie(response, "refreshToken", null, 0);
        redisUtil.deleteData("RT:" + userId);
    }

    // 밀리초를 LocalDateTime 으로 변환하는 메서드
    @Override
    public LocalDateTime convertToLocalDateTime(long timestampMillis) {

        long now = new Date().getTime();
        Instant instant = Instant.ofEpochMilli(now + timestampMillis);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());  // Instant 와 ZoneId를 사용하여 LocalDateTime 객체 생성
    }

    // 아이디 찾기
    @Override
    public UserDtoRes.FindAccountDto findAccount(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.findAccountRes(user);
    }

    // 비밀번호 찾기
    @Override
    public UserDtoRes.FindPasswordDto findPassword(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.findPasswordRes(user);
    }

    // 비밀번호 찾기 후 재설정
    @Override
    public void findRestorePassword(UserDtoReq.FindRestorePasswordDto request) {

        User user = findUser(request.getId());
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // 아이디 재설정
    @Override
    public void restoreAccount(Long userId, UserDtoReq.RestoreAccountDto request) {

        User user = findUser(userId);

        if (user.getAccount().equals(request.getAccount())) {   // 본인이 사용중인 아이디 확인
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_IS_YOURS);
        } else if (duplicatedAccount(request.getAccount())) {   // 아이디 중복 확인
            throw new UserHandler(ErrorStatus.USER_ACCOUNT_DUPLICATED);
        }
        user.setAccount(request.getAccount());
    }

    // 비밀번호 재설정
    @Override
    public void restorePassword(Long userId, UserDtoReq.RestorePasswordDto request) {

        User user = findUser(userId);

        if (!passwordEncoder.matches(request.getUsedPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NOT_EQUAL);
        }

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_MATCHED);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // 닉네임 재설정
    @Override
    public void restoreNickName(Long userId, UserDtoReq.RestoreNickNameDto request) {

        User user = findUser(userId);

        if (user.getNickName().equals(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_IS_YOURS);
        }

        if (duplicatedNickName(request.getNickName())) {
            throw new UserHandler(ErrorStatus.USER_NICKNAME_DUPLICATED);
        }
        user.setNickName(request.getNickName());
    }

    // 휴대폰 번호 재설정
    @Override
    public void restorePhone(Long userId, UserDtoReq.PhoneDto request) {

        User user = findUser(userId);

        // 휴대폰 번호 "-" 제거
        request.setPhone(request.getPhone().replaceAll("-", ""));

        phoneAuth(UserDtoReq.PhoneDto.builder()
                .phone(request.getPhone())
                .build());

        if (user.getPhone().equals(request.getPhone())) {
            throw new UserHandler(ErrorStatus.USER_PHONE_IS_YOURS);
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            log.info("이미 사용중인 휴대폰 번호 등록");
            throw new UserHandler(ErrorStatus.USER_PHONE_IS_USED);
        }

        user.setPhone(request.getPhone());
    }

    // 전공 재설정
    @Override
    public void restoreMajor(Long userId, UserDtoReq.RestoreMajorDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user.getMajors().getId().equals(request.getMajorId())) {
            throw new MajorHandler(ErrorStatus.USER_MAJOR_IS_YOURS);
        }

        Majors majors = majorsRepository.findById(request.getMajorId())
                        .orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND));

        user.setMajors(majors);
    }

    // 활동내역 공개 여부 재설정
    @Override
    public UserDtoRes.ChangeAgreementDto changeAgreement(Long userId) {

        User user = findUser(userId);

        // 다른 값으로 저장되어 있다면 AGREE 로 설정
        if (user.getAgreement() == Agreement.AGREE) {
            user.setAgreement(Agreement.DISAGREE);
        } else {
            user.setAgreement(Agreement.AGREE);
        }
        return UserConverter.changeAgreementRes(user.getAgreement());
    }

    // 프로필 사진 변경
    @Override
    public UserDtoRes.RestorePic restorePic(Long userId, MultipartFile request) {

        String fileName;

        User user = findUser(userId);

        // 사진 변경
        if (request != null) {
            log.info("request != null");
            try {
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

                fileName = amazonS3Util.upload(request, amazonConfig.getProfilePath(), savedUuid);
                log.info("사진 업로드 성공 : {}", fileName);
            } catch (IOException e) {
                throw new UserHandler(ErrorStatus.USER_FILE_CHANGE_ERROR);
            }
            // 기존 파일 삭제 후 세팅 - 디폴트라면 삭제 x
            if (!user.getPic().equals(DEFAULT_PROFILE_PATH)) {
                amazonS3Util.deleteFile(user.getPic());
            }

            user.setPic(fileName);
        }
        log.info("request == null");

        return UserDtoRes.RestorePic.builder()  // 굳이 컨버터를 거쳐야하나..?
                .userId(userId)
                .pic(user.getPic()).build();
    }

    // 프로필 사진 기본값으로 변경
    @Override
    public void restorePicDef(Long userId) {

        User user = findUser(userId);

        if (user.getPic().equals(DEFAULT_PROFILE_PATH)) {
            throw new UserHandler(ErrorStatus.PROFILE_IS_DEFAULT);
        }
        amazonS3Util.deleteFile(user.getPic());
        user.setPic(DEFAULT_PROFILE_PATH);
    }

    // 타인 프로필 조회
    @Override
    public UserDtoRes.FindProfileDto findProfile(Long userId) {

        User user = findUser(userId);

        if(user.getAgreement() == Agreement.DISAGREE) {
            return UserConverter.findProfileRes(user,null,null);
        }

        List<Board> boardListEntity = boardRepository.findTop3ByUserIdOrderByCreatedAtDesc(user.getId());

        List<BoardResponseDto.FindProfileDto> findProfileBoardDto = boardListEntity.stream()
                                                                    .map(BoardConverter::toFindProfileDto)
                                                                    .collect(Collectors.toList());
        //한게시글에 여러개의 댓글까지 생각하기
        List<Pin> pinListEntity = pinRepository.findTop30ByUserIdOrderByCreatedAtDesc(user.getId());

        //게시글 id, 제목, 좋아요 수, 댓글 수
        List<BoardResponseDto.FindProfileDto> findProfilePinDto = pinListEntity.stream()
                                    .map(pin -> pin.getBoard().getId())
                                    .distinct()
                                    .limit(3)
                                    .map(boardId -> boardRepository.findById(boardId)
                                            .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND)))
                                    .map(BoardConverter::toFindProfileDto)
                                    .collect(Collectors.toList());

        return UserConverter.findProfileRes(user,findProfileBoardDto,findProfilePinDto);
    }

    @Override
    public Page<BoardResponseDto.FindUserBoardsDto> findProfileBoards(Long userId, Pageable adjustedPageable){

        User user = findUser(userId);

        Page<Board> boardPageEntity = boardRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(),adjustedPageable);

        List<BoardResponseDto.FindUserBoardsDto> boardList = boardPageEntity.stream()
                                        .map(BoardConverter::toFindBoardsDto)
                                        .collect(Collectors.toList());

        return new PageImpl<>(boardList, adjustedPageable, boardList.size());
    }

    public Page<BoardResponseDto.FindUserBoardsDto> findProfileComments(Long userId, Pageable adjustedPageable){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Page<Pin> pagePinEntity = pinRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(),adjustedPageable);

        //map으로 Board를 조회하고 조회한 값들을 pagePin과 같이 dto에 넣는다.
        List<BoardResponseDto.FindUserBoardsDto> boardsDto = pagePinEntity.stream()
                                    .distinct()
                                    .map(pin -> new PinBoardDto(pin,boardRepository.findAllById(pin.getBoard().getId())))
                                    .map(pinBoardDto -> BoardConverter.toFindCommentsDto(pinBoardDto.getPin(),pinBoardDto.getBoard()))
                                    .collect(Collectors.toList());

        return new PageImpl<>(boardsDto, adjustedPageable, boardsDto.size());

    }

    @Override
    public Page<BoardResponseDto.HistoryDto> findHistoryAll(Long userId, Pageable pageable){

        User user = findUser(userId);

        //자기가 쓴 글
        List<BoardResponseDto.HistoryDto> boardsDto = boardRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                                        .map(board -> UserConverter.toHistoryRes(board, "내가 쓴 글", board.getCreatedAt()))
                                        .toList();
        //자기가 댓글 단 글
        List<BoardResponseDto.HistoryDto> pinBoardsDto = pinRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                                        .map(pin -> new PinBoardDto(pin, boardRepository.findById(pin.getBoard().getId())
                                                .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                                        .distinct()
                                        .map(pinBoardDto -> UserConverter.toHistoryRes(pinBoardDto.getBoard(), "댓글 단 글", pinBoardDto.getPin().getCreatedAt()))
                                        .toList();
        //자기가 좋아요 누른 글
        List<BoardResponseDto.HistoryDto> likeBoardsDto = boardLikeRepository.findAllByUserIdOrderByIdDesc(user.getId()).stream()
                                        .map(like -> new LikeBoardDto(like, boardRepository.findById(like.getBoard().getId())
                                                .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                                        .distinct()
                                        .map(likeBoardDto -> UserConverter.toHistoryRes(likeBoardDto.getBoard(), "좋아요 단 글",likeBoardDto.getLike().getCreatedAt()))
                                        .toList();

        // 세 개의 리스트를 합치고 시간 순으로 정렬
        List<BoardResponseDto.HistoryDto> mergedList = Stream.concat(Stream.concat(boardsDto.stream(), pinBoardsDto.stream()), likeBoardsDto.stream())
                                        .distinct()
                                        .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt())) // 시간 순으로 정렬
                                        .collect(Collectors.toList());

        // 전체 결과를 페이징하여 반환
        int start = Math.min((int) pageable.getOffset(), mergedList.size());
        int end = Math.min((start + pageable.getPageSize()), mergedList.size());

        return new PageImpl<>(mergedList.subList(start, end), pageable, mergedList.size());
    }

    // 내가 쓴글 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findMyBoards(Long userId, Pageable adjustedPageable) {

        User user = findUser(userId);
        //자기가 쓴 글
        Page<Board> boardPage = boardRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(),adjustedPageable);

        List<BoardResponseDto.HistoryDto> historyDtoList = boardPage.stream()
                .map(board -> UserConverter.toHistoryRes(board, "내가 쓴 글", board.getCreatedAt()))
                .collect(Collectors.toList());

        return new PageImpl<>(historyDtoList, adjustedPageable, boardPage.getTotalElements());
    }

    // 내가 쓴 댓글 글 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findMyComments(Long userId, Pageable adjustedPageable) {

        User user = findUser(userId);

        // 자기가 댓글 단 글
        Page<Pin> pinPage = pinRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), adjustedPageable);
        Page<Comment> commentPage = commentRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), adjustedPageable);

        // 댓글 단 글
        List<BoardResponseDto.HistoryDto> pinBoardsDto = pinPage.stream()
                .map(pin -> new PinBoardDto(pin, boardRepository.findById(pin.getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(pinBoardDto -> UserConverter.toHistoryCommentRes(pinBoardDto.getBoard(), "댓글 단 글", pinBoardDto.getPin().getCreatedAt(), pinBoardDto.getPin().getComment()))
                .toList();

        // 대댓글 단 글
        List<BoardResponseDto.HistoryDto> commentBoardsDto = commentPage.stream()
                .map(comment -> new CommentBoardDto(comment, boardRepository.findById(comment.getPin().getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(commentBoardDto -> UserConverter.toHistoryCommentRes(commentBoardDto.getBoard(), "댓글 단 글", commentBoardDto.getComment().getCreatedAt(), commentBoardDto.getComment().getComment()))
                .toList();

        // 두 리스트 합치기
        List<BoardResponseDto.HistoryDto> combinedList = new ArrayList<>();
        combinedList.addAll(pinBoardsDto);
        combinedList.addAll(commentBoardsDto);

        // 최신순으로 정렬
        List<BoardResponseDto.HistoryDto> sortedList = combinedList.stream()
                .sorted(Comparator.comparing(BoardResponseDto.HistoryDto::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(sortedList, adjustedPageable, sortedList.size());
    }

    // 내가 좋아요 단 글 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findMyLikes(Long userId, Pageable adjustedPageable) {

        User user = findUser(userId);

        //자기가 좋아요 누른 글
        List<BoardLike> likePage = boardLikeRepository.findAllByUserIdOrderByIdDesc(user.getId(),adjustedPageable);

        List<BoardResponseDto.HistoryDto> LikeBoardsDto = likePage.stream()
                                    .map(like -> new LikeBoardDto(like, boardRepository.findById(like.getBoard().getId())
                                            .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                                    .distinct()
                                    .map(likeBoardDto -> UserConverter.toHistoryRes(likeBoardDto.getBoard(), "좋아요 단 글",likeBoardDto.getLike().getCreatedAt()))
                                    .collect(Collectors.toList());

        return new PageImpl<>(LikeBoardsDto, adjustedPageable, likePage.size());
    }

    // 활동내역 전체 검색 조회 (내가 쓴글,댓글 단글, 좋아요)
    @Override
    public Page<BoardResponseDto.HistoryDto> findTextHistoryAll(Long userId, Pageable adjustedPageable,String content){

        User user = findUser(userId);

        //자기가 쓴 글
        List<BoardResponseDto.HistoryDto> boardsDto = boardRepository.findAllByUserIdAndContentContainingOrUserIdAndTitleContainingOrderByCreatedAtDesc(user.getId(),content,user.getId(),content,adjustedPageable).stream()
                .map(board -> UserConverter.toHistoryRes(board, "내가 쓴 글", board.getCreatedAt()))
                .toList();
        //자기가 댓글 단 글
        List<BoardResponseDto.HistoryDto> pinBoardsDto = pinRepository.findAllByUserIdAndCommentContainingOrderByCreatedAtDesc(user.getId(),content, adjustedPageable).stream()
                .map(pin -> new PinBoardDto(pin, boardRepository.findById(pin.getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(pinBoardDto -> UserConverter.toHistoryCommentRes(pinBoardDto.getBoard(), "댓글 단 글", pinBoardDto.getPin().getCreatedAt(), pinBoardDto.getPin().getComment()))
                .toList();
        // 대댓글 단 글 섞기
        List<BoardResponseDto.HistoryDto> commentBoardsDto = commentRepository.findAllByUserIdAndCommentContainingOrderByCreatedAtDesc(user.getId(),content, adjustedPageable).stream()
                .map(comment -> new CommentBoardDto(comment, boardRepository.findById(comment.getPin().getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(commentBoardDto -> UserConverter.toHistoryCommentRes(commentBoardDto.getBoard(), "댓글 단 글", commentBoardDto.getComment().getCreatedAt(), commentBoardDto.getComment().getComment()))
                .toList();
        //자기가 좋아요 누른 글
        List<BoardResponseDto.HistoryDto> likeBoardsDto = boardLikeRepository.findAllByUserIdOrderByIdDesc(user.getId(), adjustedPageable).stream()
                .map(like -> {
                    Board board = boardRepository.findById(like.getBoard().getId())
                            .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
                    if(board.getContent().contains(content) || board.getTitle().contains(content)){
                            return new LikeBoardDto(like, board);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .map(likeBoardDto -> UserConverter.toHistoryRes(likeBoardDto.getBoard(), "좋아요 단 글",likeBoardDto.getLike().getCreatedAt()))
                .toList();

        // 세 개의 리스트 합치기
        List<BoardResponseDto.HistoryDto> combinedList = new ArrayList<>();
        combinedList.addAll(boardsDto);
        combinedList.addAll(pinBoardsDto);
        combinedList.addAll(likeBoardsDto);
        combinedList.addAll(commentBoardsDto);

        // 최신순으로 정렬
        List<BoardResponseDto.HistoryDto> sortedList = combinedList.stream()
                .sorted(Comparator.comparing(BoardResponseDto.HistoryDto::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(sortedList, adjustedPageable, sortedList.size());
    }

    // 활동내역 내가쓴글 검색 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findTextHistoryBoards(Long userId, Pageable adjustedPageable, String content){

        User user = findUser(userId);

        //자기가 쓴 글
        List<BoardResponseDto.HistoryDto> boardsDto = boardRepository.findAllByUserIdAndContentContainingOrUserIdAndTitleContainingOrderByCreatedAtDesc(user.getId(),content,user.getId(),content,adjustedPageable).stream()
                .map(board -> UserConverter.toHistoryRes(board, "내가 쓴 글", board.getCreatedAt()))
                .collect(Collectors.toList());

        return new PageImpl<>(boardsDto, adjustedPageable, boardsDto.size());
    }

    // 활동내역 댓글 검색 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findTextHistoryComments(Long userId, Pageable adjustedPageable, String content) {

        User user = findUser(userId);
        //자기가 댓글 단 글
        List<BoardResponseDto.HistoryDto> pinBoardsDto = pinRepository.findAllByUserIdAndCommentContainingOrderByCreatedAtDesc(user.getId(),content, adjustedPageable).stream()
                .map(pin -> new PinBoardDto(pin, boardRepository.findById(pin.getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(pinBoardDto -> UserConverter.toHistoryCommentRes(pinBoardDto.getBoard(), "댓글 단 글", pinBoardDto.getPin().getCreatedAt(), pinBoardDto.getPin().getComment()))
                .toList();
        // 대댓글 단 글 섞기
        List<BoardResponseDto.HistoryDto> commentBoardsDto = commentRepository.findAllByUserIdAndCommentContainingOrderByCreatedAtDesc(user.getId(),content, adjustedPageable).stream()
                .map(comment -> new CommentBoardDto(comment, boardRepository.findById(comment.getPin().getBoard().getId())
                        .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND))))
                .distinct()
                .map(commentBoardDto -> UserConverter.toHistoryCommentRes(commentBoardDto.getBoard(), "댓글 단 글", commentBoardDto.getComment().getCreatedAt(), commentBoardDto.getComment().getComment()))
                .toList();

        List<BoardResponseDto.HistoryDto> combinedList = new ArrayList<>();
        combinedList.addAll(commentBoardsDto);
        combinedList.addAll(pinBoardsDto);

        List<BoardResponseDto.HistoryDto> sortedList = combinedList.stream()
                .sorted(Comparator.comparing(BoardResponseDto.HistoryDto::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return new PageImpl<>(sortedList, adjustedPageable, sortedList.size());
    }

    // 활동내역 좋아요 검색 조회
    @Override
    public Page<BoardResponseDto.HistoryDto> findTextHistoryLikes(Long userId, Pageable adjustedPageable, String content) {

        User user = findUser(userId);

        //자기가 좋아요 누른 글
        List<BoardResponseDto.HistoryDto> likeBoardsDto = boardLikeRepository.findAllByUserIdOrderByIdDesc(user.getId(), adjustedPageable).stream()
                .map(like -> {
                    Board board = boardRepository.findById(like.getBoard().getId())
                            .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
                    if(board.getContent().contains(content) || board.getTitle().contains(content)){
                        return new LikeBoardDto(like, board);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .map(likeBoardDto -> UserConverter.toHistoryRes(likeBoardDto.getBoard(), "좋아요 단 글",likeBoardDto.getLike().getCreatedAt()))
                .collect(Collectors.toList());

        return new PageImpl<>(likeBoardsDto, adjustedPageable, likeBoardsDto.size());
    }

    // 사용자 찾기
    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 사용자 존재 유무
    @Override
    public boolean existUser(Long userId) {
        return userRepository.existsById(userId);
    }

    //본인 전공 검색
    public UserDtoRes.getMajor getMajor(Long userId){
        User user = findUser(userId);

        return new UserDtoRes.getMajor(user.getMajors().getMajor());
    }
}
