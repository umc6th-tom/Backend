package umc6.tom.alarm.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {// 내가 쓴 글, 댓글 단 글, 좋아요
    WrittenBoard("내가 쓴 글"), commented("댓글 단 글"), liked("좋아요");

    private final String kor;

}
