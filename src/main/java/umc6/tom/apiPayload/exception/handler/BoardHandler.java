package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class BoardHandler extends GeneralException {
    public BoardHandler(BaseErrorCode errorCode) {super(errorCode);}
}
