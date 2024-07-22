package umc6.tom.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    USER_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "USER4002", "사용자는 활성 상태가 아닙니다."),
    USER_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "USER4003", "닉네임이 이미 존재합니다."),
    USER_ACCOUNT_DUPLICATED(HttpStatus.BAD_REQUEST, "USER4004", "이미 등록된 아이디입니다."),
    USER_PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "USER4005", "사용자의 비밀번호와 일치하지 않습니다."),
    USER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "USER4006", "이미 삭제된 사용자입니다."),
    USER_ALREADY_WITHDRAW(HttpStatus.BAD_REQUEST, "USER4007", "이미 탈퇴 요청한 사용자입니다."),
    USER_ACCOUNT_NOT_MATCHED(HttpStatus.BAD_REQUEST,"USER4008", "전화번호로 찾은 계정의 아이디와 일치하지 않습니다."),
    USER_NAME_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER4009", "전화번호로 찾은 계정의 사용자 이름과 일치하지 않습니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER4010", "비밀번호 비밀번호 확인이 맞지 않습니다."),
    USER_ACCOUNT_IS_YOURS(HttpStatus.BAD_REQUEST, "USER4011", "본인이 사용중인 아이디입니다."),
    USER_NICKNAME_IS_YOURS(HttpStatus.BAD_REQUEST, "USER4012", "본인이 사용중인 닉네임입니다."),
    USER_PHONE_IS_YOURS(HttpStatus.BAD_REQUEST, "USER4013", "본인이 사용중인 휴대폰번호입니다."),
    USER_PHONE_IS_USED(HttpStatus.BAD_REQUEST, "USER4014", "이미 사용중인 휴대폰번호입니다. 관리자에게 문의하세요"),




    // 인증 관련 에러
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH4001", "휴대폰 인증이 필요합니다."),


    // 전공 관련 에러
    MAJORS_NOR_FOUND(HttpStatus.NOT_FOUND, "MAJORS4001", "해당하는 전공이 없습니다."),
    USER_MAJOR_IS_YOURS(HttpStatus.BAD_REQUEST, "MAJORS4002", "아마 사용자의 전공입니다."),

    // 토큰 관련 에러
    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT4001", "권한이 없습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT4002", "다시 로그인 해주세요."),
    JWT_EMPTY(HttpStatus.UNAUTHORIZED, "JWT4003", "JWT 토큰을 넣어주세요."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4004", "만료된 토큰입니다."),

    // 게시판 관련 에러
    BoardLike_DUPLICATED(HttpStatus.UNAUTHORIZED, "BOARD4001", "이미 좋아요 되었습니다."),
    BoardLike_NOT_FOUND(HttpStatus.UNAUTHORIZED, "BOARD4002", "이미 좋아요가 취소되었습니다. "),

    // 댓글 관련 에러
    PIN_NOT_REGISTER(HttpStatus.INTERNAL_SERVER_ERROR, "PIN4001", "댓글 등록에 실패했습니다."),
    PIN_NOT_FOUND(HttpStatus.NOT_FOUND, "PIN4002", "댓글을 찾을 수 없습니다."),
    PIN_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "PIN4003", "댓글을 삭제하는데 실패하였습니다."),



    // 예제 관련 에러
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITE4001", "즐겨찾기 예제를 찾는것에 실패하였습니다."),
    FAVORITE_NOT_REGISTER(HttpStatus.INTERNAL_SERVER_ERROR, "FAVORITE4003", "즐겨찾기 등록에 실패했습니다."),
    EXAMPLE_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "EXAMPLE4002", "예제 삭제에 실패했습니다."),
    EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "EXAMPLE4004" , "예제를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
