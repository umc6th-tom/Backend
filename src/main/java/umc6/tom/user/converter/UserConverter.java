package umc6.tom.user.converter;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;
import umc6.tom.common.model.Majors;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Agreement;
import umc6.tom.user.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {

    // 회원 객체 생성
    public static User toUser(UserDtoReq.JoinDto request, Majors major, String defaultProfilePAth) {

        return User.builder()
                .name(request.getName())
                .nickName(request.getNickName())
                .account(request.getAccount())
                .password(request.getPassword())
                .phone(request.getPhone())
                .agreement(Agreement.AGREE)
                .pic(defaultProfilePAth)
                .majors(major)
                .status(UserStatus.ACTIVE)
                .build();
    }

    // 휴대폰 인증 응답
    public static UserDtoRes.PhoneAuthDto phoneAuth(String phone) {

        return UserDtoRes.PhoneAuthDto.builder()
                .phone(phone)
                .build();
    }


    // 회원가입 응답
    public static UserDtoRes.JoinDto joinRes(User user) {

        return UserDtoRes.JoinDto.builder()
                .id(user.getId())
                .nickName(user.getUsername())
                .build();
    }

    // 로그인 응답
    public static UserDtoRes.LoginDto signInRes(User user, String accessToken, String refreshToken) {

        return UserDtoRes.LoginDto.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

    // accessToken 재발급 응답
    public static UserDtoRes.ReissueDto reissueRes(String accessToken) {

        return UserDtoRes.ReissueDto.builder()
                .accessToken(accessToken)
                .build();
    }

    // 아이디 찾기 응답
    public static UserDtoRes.FindAccountDto findAccountRes(User user) {

        return UserDtoRes.FindAccountDto.builder()
                .account(user.getAccount())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 비밀번호 찾기 응답
    public static UserDtoRes.FindPasswordDto findPasswordRes(User user) {

        return UserDtoRes.FindPasswordDto.builder()
                .userId(user.getId())
                .build();
    }

    // 활동내역 공개 여부 변경 응답
    public static UserDtoRes.ChangeAgreementDto changeAgreementRes(Agreement agreement) {

        return UserDtoRes.ChangeAgreementDto.builder()
                .agreement(agreement.name())
                .build();
    }

    // 타인 프로필 조회 응답
    public static UserDtoRes.ProfileDto toProfileRes(User user, List<Board> boards, List<Board> pins) {

        return UserDtoRes.ProfileDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .pic(user.getPic())
                .message(user.getDescription())
                .written(boards)
                .commented(pins)
                .build();
    }


    // 타인 프로필 조회 정상 응답
    public static UserDtoRes.FindProfileDto findProfileRes(User user, List<BoardResponseDto.FindProfileDto> boards, List<BoardResponseDto.FindProfileDto> pins) {
        // 타인 프로필 조회 응답 비공계 계정일경우
        if(boards == null && pins == null){
            return UserDtoRes.FindProfileDto.builder()
                    .userId(user.getId())
                    .nickName(user.getNickName())
                    .pic(user.getPic())
                    .description(user.getDescription())
                    .build();
        }

        return UserDtoRes.FindProfileDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .pic(user.getPic())
                .description(user.getDescription())
                .board(boards)
                .pinBoard(pins)
                .build();
    }

    //나의 내역 보기
    public static BoardResponseDto.HistoryDto toHistoryRes(Board board, String header, LocalDateTime createdAt) {

        return BoardResponseDto.HistoryDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(new DateCalc().formatDate(createdAt))
                .header(header)
                .build();
    }
}
