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
    USER_ACCOUNT_DUPLICATED(HttpStatus.BAD_REQUEST, "USER4004", "이미 가입된 아이디입니다."),
    USER_PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "USER4005", "비밀번호가 일치하지 않습니다."),

    USER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "USER4006", "이미 삭제된 사용자입니다."),

    // 휴대폰 인증 관련 에러
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "PHONE4001", "휴대폰 인증이 필요합니다."),

    // 전공 관련 에러
    MAJORS_NOR_FOUND(HttpStatus.NOT_FOUND, "MAJORS", "해당하는 전공이 없습니다.");


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
