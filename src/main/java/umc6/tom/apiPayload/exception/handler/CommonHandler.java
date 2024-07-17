package umc6.tom.apiPayload.exception.handler;


import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class CommonHandler extends GeneralException {
    public CommonHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
