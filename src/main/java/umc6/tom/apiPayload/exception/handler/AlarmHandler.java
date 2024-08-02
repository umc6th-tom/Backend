package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class AlarmHandler extends GeneralException {
    public AlarmHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
