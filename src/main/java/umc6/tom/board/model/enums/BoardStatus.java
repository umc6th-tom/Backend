package umc6.tom.board.model.enums;

public enum BoardStatus {
    //활성화, 신고(1번이라도 된 상태), 신고상태에서 삭제(데이터는 남아있고 화면에 안보임), 일정 횟수이상 신고시 안보이게
    ACTIVE, COMPLAINT, COMPLAINTDELETE, OVERCOMPLAINT
}
