package umc6.tom.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.ProhibitHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.repository.BoardComplaintRepository;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.converter.CommentComplaintConverter;
import umc6.tom.comment.converter.PinConverter;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.comment.model.PinComplaint;
import umc6.tom.comment.repository.CommentComplaintRepository;
import umc6.tom.comment.repository.PinComplaintRepository;
import umc6.tom.user.converter.UserConverter;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.Prohibit;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Role;
import umc6.tom.user.repository.ProhibitRepository;
import umc6.tom.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional
public class RootUserServiceImpl implements RootUserService {

    private final UserRepository userRepository;
    private final ProhibitRepository prohibitRepository;
    private final BoardRepository boardRepository;
    private final CommentComplaintRepository commentComplaintRepository;
    private final BoardComplaintRepository boardComplaintRepository;
    private final PinComplaintRepository pinComplaintRepository;


    // 관리자의 권한을 갖는지 검증하는 메서드
    @Override
    public void managerAuth(Long userId) {
        if (!Role.ADMIN.equals(userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND))
                .getRole())) {
            throw new UserHandler(ErrorStatus.NOT_ADMIN);
        }
    }

    // 사용자 찾기
    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 경고 부여
    @Override
    public UserDtoRes.warnDto warn(Long userId, UserDtoReq.WarnDto request) {

        managerAuth(userId);

        User user = findUser(request.getTargetUserId());

        Prohibit prohibit = prohibitRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new ProhibitHandler(ErrorStatus.PROHIBIT_NOT_FOUND));

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));


        prohibit.setDivision(request.getDivision());
        prohibit.setBoard(board);

        user.setWarn(user.getWarn() + 1);

        return UserConverter.toWarnDto(request.getTargetUserId(), user.getNickName(), "귀하는 " +request.getMessage() + "의 이유로 커뮤니티 가이드라인을 위반 하였고, 이에 1회 경고를 부여합니다.");
    }

    // 회원 정지
    @Override
    public UserDtoRes.suspendDto suspension(Long userId, UserDtoReq.SuspendDto request) {

        managerAuth(userId);

        User user = findUser(request.getTargetUserId());

        Prohibit prohibit = prohibitRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new ProhibitHandler(ErrorStatus.PROHIBIT_NOT_FOUND));

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() ->new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));

        prohibit.setDivision(request.getDivision());
        prohibit.setBoard(board);

        prohibit.setSuspensionDue(LocalDateTime.now().plusDays(request.getSuspensionDueInt()));

        user.setSuspension(user.getSuspension() + 1);

        return UserDtoRes.suspendDto.builder()
                .message("귀하는 " + request.getMessage() + "의 이유로 커뮤니티 가이드라인을 위반 하였고, 이에 " + request.getSuspensionDueInt() + "일 정지에 해당하는 조취를 취하는 바이다.")
                .nickName(user.getNickName())
                .userId(request.getTargetUserId())
                .build();
    }

    public Page<UserDtoRes.userFindAllDto> findAllUser(String keyword, Pageable adjustedPageable){
        Page<User> userPageEntity = userRepository.findAllByNameContainingOrAccountContainingOrNickNameContainingOrderByCreatedAtDesc(keyword,keyword,keyword,adjustedPageable);

        List<UserDtoRes.userFindAllDto> userDtoList = userPageEntity.stream()
                .map(UserConverter::toUsersFindDto)
                .toList();

        return new PageImpl<>(userDtoList, adjustedPageable, userDtoList.size());
    }

    public Page<UserDtoRes.userFindAllDto> findNicknameUser(String keyword, Pageable adjustedPageable){
        Page<User> userPageEntity = userRepository.findAllByNickNameContainingOrderByCreatedAtDesc(keyword,adjustedPageable);

        List<UserDtoRes.userFindAllDto> userDtoList = userPageEntity.stream()
                .map(UserConverter::toUsersFindDto)
                .toList();

        return new PageImpl<>(userDtoList, adjustedPageable, userDtoList.size());
    }

    public Page<UserDtoRes.userFindAllDto> findNameUser(String keyword, Pageable adjustedPageable){
        Page<User> userPageEntity = userRepository.findAllByNameContainingOrderByCreatedAtDesc(keyword,adjustedPageable);

        List<UserDtoRes.userFindAllDto> userDtoList = userPageEntity.stream()
                .map(UserConverter::toUsersFindDto)
                .toList();

        return new PageImpl<>(userDtoList, adjustedPageable, userDtoList.size());
    }

    public Page<UserDtoRes.userFindAllDto> findAccountUser(String keyword, Pageable adjustedPageable){
        Page<User> userPageEntity = userRepository.findAllByAccountContainingOrderByCreatedAtDesc(keyword,adjustedPageable);

        List<UserDtoRes.userFindAllDto> userDtoList = userPageEntity.stream()
                .map(UserConverter::toUsersFindDto)
                .toList();

        return new PageImpl<>(userDtoList, adjustedPageable, userDtoList.size());
    }

    //root 유저 프로필 조회
    public UserDtoRes.userFindDetailDto findUserDetail(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<BoardComplaint> boardComplaints = boardComplaintRepository.findAllByBoardUserId(user.getId());
        List<PinComplaint> pinComplaints = pinComplaintRepository.findAllByPinUserId(user.getId());
        List<CommentComplaint> commentComplaints = commentComplaintRepository.findAllByCommentUserId(user.getId());

        //게시판 3개
        List<BoardComplaint> boardComplaintList = boardComplaints.stream()
                .sorted(Comparator.comparing(BoardComplaint::getCreatedAt).reversed())
                .limit(3)
                .toList();

        List<PinResDto.RootUserDetailPinsDto> complaintPin = pinComplaints.stream()
                .sorted(Comparator.comparing(PinComplaint::getCreatedAt).reversed())
                .limit(3)
                .map(pinComplaint -> CommentComplaintConverter.rootUserFindDetail(pinComplaint.getId(), pinComplaint.getPin().getBoard().getId(), pinComplaint.getPinComment(),pinComplaint.getCreatedAt()))
                .toList();

        List<PinResDto.RootUserDetailPinsDto> complaintComment = commentComplaints.stream()
                .sorted(Comparator.comparing(CommentComplaint::getCreatedAt).reversed())
                .limit(3)
                .map(CommentComplaint -> CommentComplaintConverter.rootUserFindDetail(CommentComplaint.getId(), CommentComplaint.getComment().getPin().getBoard().getId(), CommentComplaint.getCommentComment(),CommentComplaint.getCreatedAt()))
                .toList();

        List<PinResDto.RootUserDetailPinsDto> top3ComplaintPinCommentList = Stream.concat(complaintPin.stream(), complaintComment.stream())
                .sorted(Comparator.comparing(PinResDto.RootUserDetailPinsDto::getCreatedAt).reversed())
                .limit(3)
                .toList();


        return UserConverter.userFindDetailDto(user,boardComplaintList,top3ComplaintPinCommentList,boardComplaints.size(),pinComplaints.size() + commentComplaints.size());
    }


    //유저의 신고당한 글 리스트 보기
    public Page<BoardResponseDto.RootUserReportBoardsDto> findUserReportBoards(Long boardUserId, Pageable adjustPageable){
        Page<BoardComplaint> boardComplaints = boardComplaintRepository.findAllByBoardUserIdOrderByCreatedAtDesc(boardUserId,adjustPageable);

        List<BoardResponseDto.RootUserReportBoardsDto> reportBoards = boardComplaints.stream()
                .collect(Collectors.toMap(
                        BoardComplaint::getId, // 중복을 제거할 기준이 되는 키
                        Function.identity(),                           // 값을 그대로 사용
                        (existing, replacement) -> existing            // 중복 키가 있을 경우 기존 값을 유지
                ))
                .values().stream() // 중복이 제거된 값들을 스트림으로 변환
                .map(BoardConverter::rootUserReportBoardsDto)
                .sorted(Comparator.comparing(BoardResponseDto.RootUserReportBoardsDto::getTestCreatedAt).reversed()) // 시간순으로 정렬
                .toList();

        return new PageImpl<>(reportBoards, adjustPageable, boardComplaints.getTotalElements());
    }

    //유저의 신고당한 댓글 리스트 보기
    public Page<PinResDto.RootUserReportPinsOrCommentsPinsDto> findUserReportPins(Long writeUserId, Pageable adjustedPageable){
        List<PinComplaint> pinComplaints = pinComplaintRepository.findAllByPinUserIdOrderByCreatedAtDesc(writeUserId);
        List<CommentComplaint> commentComplaints = commentComplaintRepository.findAllByCommentUserIdOrderByCreatedAtDesc(writeUserId);

        List<PinResDto.RootUserReportPinsOrCommentsPinsDto> pinComplaintDtos = pinComplaints.stream()
                .collect(Collectors.toMap(
                        pinComplaint -> pinComplaint.getPin().getId(), // 중복을 제거할 기준이 되는 키
                        Function.identity(),                           // 값을 그대로 사용
                        (existing, replacement) -> existing            // 중복 키가 있을 경우 기존 값을 유지
                ))
                .values().stream() // 중복이 제거된 값들을 스트림으로 변환
                .map(pinComplaint -> PinConverter.rootUserReportPinsOrCommentsPinsDto(pinComplaint.getPin().getId(),pinComplaint.getPinComment(),pinComplaint.getCreatedAt(),"pin"))
                .toList();

        List<PinResDto.RootUserReportPinsOrCommentsPinsDto> commentComplaintDtos = commentComplaints.stream()
                .collect(Collectors.toMap(
                        commentComplaint -> commentComplaint.getComment().getId(), // 중복을 제거할 기준이 되는 키
                        Function.identity(),                           // 값을 그대로 사용
                        (existing, replacement) -> existing            // 중복 키가 있을 경우 기존 값을 유지
                ))
                .values().stream() // 중복이 제거된 값들을 스트림으로 변환
                .map(commentComplaint -> PinConverter.rootUserReportPinsOrCommentsPinsDto(commentComplaint.getComment().getId(),commentComplaint.getCommentComment(),commentComplaint.getCreatedAt(),"comment"))
                .toList();

        List<PinResDto.RootUserReportPinsOrCommentsPinsDto> complaintPinCommentList = Stream.concat(pinComplaintDtos.stream(), commentComplaintDtos.stream())
                .sorted(Comparator.comparing(PinResDto.RootUserReportPinsOrCommentsPinsDto::getCreatedAt).reversed())
                .toList();

        // 총 데이터 개수
        int totalElements = complaintPinCommentList.size();

        // 현재 페이지에 대한 시작 인덱스 계산
        int startIndex = (int) adjustedPageable.getOffset();
        int endIndex = Math.min((startIndex + adjustedPageable.getPageSize()), totalElements);

        // 부분 리스트 생성
        List<PinResDto.RootUserReportPinsOrCommentsPinsDto> pageContent = complaintPinCommentList.subList(startIndex, endIndex);

        // PageImpl 생성
        return new PageImpl<>(pageContent, adjustedPageable, totalElements);
    }
}
