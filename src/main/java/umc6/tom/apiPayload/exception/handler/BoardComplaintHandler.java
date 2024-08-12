package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class BoardComplaintHandler extends GeneralException {
    public BoardComplaintHandler(BaseErrorCode errorCode) {super(errorCode);}
}