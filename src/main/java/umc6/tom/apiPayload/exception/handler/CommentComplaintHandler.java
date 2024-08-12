package umc6.tom.apiPayload.exception.handler;

import umc6.tom.apiPayload.code.BaseErrorCode;
import umc6.tom.apiPayload.exception.GeneralException;

public class CommentComplaintHandler extends GeneralException {
    public CommentComplaintHandler(BaseErrorCode errorCode) {super(errorCode);}
}