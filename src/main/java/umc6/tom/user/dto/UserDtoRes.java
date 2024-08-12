package umc6.tom.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.model.Board;
import umc6.tom.comment.dto.PinResDto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    public static class JoinDto {

        private Long id;
        private String nickName;
    }

    @Setter
    @Getter
    @Builder
    public static class PhoneAuthDto {

        private String phone;
        private String authNum;
    }

    @Getter
    @Setter
    @Builder
    public static class LoginDto {

        private Long userId;
        private String nickName;
        private Long majorId;
        private String accessToken;
        private String refreshToken;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class ReissueDto {

        private Long userId;
        private String nickName;
        private Long majorId;
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    public static class FindAccountDto {

        private String account;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    public static class FindPasswordDto {

        private Long userId;
        private String account;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class ChangeAgreementDto {

        private String agreement;
    }

    @Getter
    @Setter
    @Builder
    public static class ProfileDto {

        private Long userId;
        private String pic;
        private String nickName;
        private String description;
        private String agreement;
        private List<Board> written;
        private List<Board> commented;
        private String message;
    }

    @Getter
    @Setter
    @Builder
    public static class RestorePic {

        private Long userId;
        private String pic;
    }

    @Getter
    @Setter
    @Builder
    public static class FindProfileDto {
        private Long userId;
        private String nickName;
        private String description;
        private String pic;
        private List<BoardResponseDto.FindProfileDto> board;
        private List<BoardResponseDto.FindProfileDto> pinBoard;
    }

    @Getter
    @Setter
    @Builder
    public static class FindDisAgreeProfileDto {
        private Long userId;
        private String nickName;
        private String description;
        private String pic;
    }

    @Getter
    @Setter
    @Builder
    public static class warnDto {
        private Long userId;
        private String nickName;
        private String message;
    }

    @Setter
    @Getter
    @Builder
    public static class userFindAllDto {
        private Long userId;
        private String name;
        private String nickName;
        private String account;
        private String createdAt;
        private String pic;
        private Long warn;
        private Long report;
        private Long stop;
    }

    @Setter
    @Getter
    @Builder
    public static class userFindDetailDto {
        private Long userId;
        private String nickName;
        private String name;
        private String pic;
        private Integer warn;
        private Integer report;
        private Integer stop;
        private List<BoardResponseDto.RootUserDetailDto> boards;
        private int boardReportCount;
        private List<PinResDto.RootUserDetailPinsDto> pins;
        private int pinsReportCount;
    }


    @Getter
    @Setter
    @Builder
    public static class suspendDto {
        private Long userId;
        private String nickName;
        private String message;
    }

    @Getter
    @Setter
    @Builder
    public static class complaintBoardReasonDto {
        private Long userId;
        private Long boardId;
        private String nickname;
        private String userPic;
        private String createdAt;
        private int report;
        private String boardTitle;
        private String boardContent;
        private List<String> boardPic;

        private List<complaintReasonDto> complaint;
    }

    @Getter
    @Setter
    @Builder
    public static class complaintReasonDto {
        private Long userId;
        private String nickname;
        private String userPic;
        private String createdAt;
        private String complaintContent;
    }
}
