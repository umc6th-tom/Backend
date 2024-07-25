package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
