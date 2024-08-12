package umc6.tom.comment.model.enums;

public enum PinBoardStatus {
    //활성화, 신고상태에서 삭제(데이터는 남아있고 화면에 안보임), 일정 횟수이상 신고시 안보이게, 대댓글이 있을 때 삭제했을경우
    ACTIVE, COMPLAINTDELETE, OVERCOMPLAINT, INACTIVE;
}
