package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class ProhibitHandler extends GeneralException {
    public ProhibitHandler(BaseErrorCode errorCode) {super(errorCode);}
}
