package umc6.tom.user.converter;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.common.model.Majors;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Agreement;
import umc6.tom.user.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
                .nickName(user.getNickName())
                .majorId(user.getMajors().getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

    // accessToken 재발급 응답
    public static UserDtoRes.ReissueDto reissueRes(String accessToken, User user) {

        return UserDtoRes.ReissueDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .majorId(user.getMajors().getId())
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
                .account(user.getAccount())
                .name(user.getName())
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

    //나의 내역 보기
    public static BoardResponseDto.HistoryDto toHistoryCommentRes(Board board, String header, LocalDateTime createdAt, String comment) {

        return BoardResponseDto.HistoryDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(comment)
                .createdAt(new DateCalc().formatDate(createdAt))
                .header(header)
                .build();
    }

    //나의 내역 보기
    public static BoardResponseDto.HistoryCommentDto toHistoryRes(Board board, String header, LocalDateTime createdAt,String comment) {

        return BoardResponseDto.HistoryCommentDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .comment(comment)
                .createdAt(new DateCalc().formatDate(createdAt))
                .header(header)
                .build();
    }

    // 경고 부여
    public static UserDtoRes.warnDto toWarnDto(Long userId, String nickName, String message) {

        return UserDtoRes.warnDto.builder()
                .userId(userId)
                .nickName(nickName)
                .message(message)
                .build();
    }

    public static UserDtoRes.userFindAllDto toUsersFindDto(User user){

        String date = DateCalc.formatDate2(user.getCreatedAt());

        return UserDtoRes.userFindAllDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .nickName(user.getNickName())
                .account(user.getAccount())
                .pic(user.getPic())
                .createdAt(date)
                .warn(Long.valueOf(user.getWarn()))
                .report(Long.valueOf(user.getReport()))
                .stop(Long.valueOf(user.getSuspension()))
                .build();
    }

    public static UserDtoRes.userFindDetailDto userFindDetailDto(User user, List<BoardComplaint> top3Boards, List<PinResDto.RootUserDetailPinsDto> top3PinComments, int boardSize, int pinCommentSize){

        return UserDtoRes.userFindDetailDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .name(user.getName())
                .pic(user.getPic())
                .warn(user.getWarn())
                .report(user.getReport())
                .boards(top3Boards.stream().map(BoardConverter::titleBoardIdDto).toList())
                .boardReportCount(boardSize)
                .pins(top3PinComments)
                .pinsReportCount(pinCommentSize)
                .stop(user.getSuspension())
                .build();
    }

}
