package umc6.tom.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
