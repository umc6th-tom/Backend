package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class AdminHandler extends GeneralException {
    public AdminHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
