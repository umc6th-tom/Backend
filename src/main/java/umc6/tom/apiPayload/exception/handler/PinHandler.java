package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseCode;
import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class PinHandler extends GeneralException {
    public PinHandler(BaseErrorCode errorCode) {super(errorCode);}
}
