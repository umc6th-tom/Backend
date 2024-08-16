package umc6.tom.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.model.enums.BoardComplaintStatus;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.user.model.enums.Role;
import umc6.tom.user.model.enums.SocialType;

import java.time.LocalDateTime;
import java.util.List;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    public static class JoinDto {

        private Long id;
        private String nickName;
        private String socialType;
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
        private Role role;
        private String accessToken;
        private UserDtoRes.suspensionDto suspension;
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
        private String status;
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
    public static class suspensionDto {
        private String nickName;
        private String message;
        private Long boardId;
        private String title;
        @Size(max = 20)
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class kakaoJoinDto {
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        private String name;
        @Size(message = "닉네임은 2글자 이상, 10글자 이하입니다.", min = 2, max = 10)
        private String nickName;
        @NotBlank(message = "아이디는 필수! 6글자 이상, 15글자 이하입니다.")
        @Size(min = 6, max = 15)
        @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
        private String phone;
        private String profilePic;
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

    @Getter
    @Setter
    @Builder
    public static class complaintCommentReasonDto {
        private Long userId;
        private Long boardId;
        private String nickname;
        private String userPic;
        private String createdAt;
        private int report;
        private String commentContent;
        private List<String> commentPic;

        private List<complaintReasonDto> complaint;
    }

    @Getter
    @Setter
    @Builder
    public static class complaintAllResDto {
        private String type;
        private Long id;
        private String title;
        private int report;
        private String createdAt;  // LocalDateTime으로 수정
        private String status;
    }

    @Getter
    @Setter
    public static class complaintAllResDto2 {
        private String type;
        private Long id;
        private String title;
        private LocalDateTime createdAt;  // LocalDateTime으로 수정
        private int report;
        private String status;

        public complaintAllResDto2(String type, Long id, String content, int report, LocalDateTime createdAt, BoardComplaintStatus status) {
            this.type = type;
            this.id = id;
            this.title = content;
            this.report = report;
            this.createdAt = createdAt;
            this.status = String.valueOf(status);
        }
    }

    @Getter
    @Setter
    public static class getMajor {
        private String major;

        public getMajor(String major) {
            this.major = major;
        }
    }

}
