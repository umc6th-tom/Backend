package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class QNAHandler extends GeneralException {
    public QNAHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
