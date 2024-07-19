package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class PhoneHandler extends GeneralException {

    public PhoneHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
