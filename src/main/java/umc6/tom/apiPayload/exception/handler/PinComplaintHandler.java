package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class PinComplaintHandler extends GeneralException {
    public PinComplaintHandler(BaseErrorCode errorCode) {super(errorCode);}
}