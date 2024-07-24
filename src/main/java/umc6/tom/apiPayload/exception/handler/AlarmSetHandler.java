package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class AlarmSetHandler extends GeneralException {
    public AlarmSetHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
