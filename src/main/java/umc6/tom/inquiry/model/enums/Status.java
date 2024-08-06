package umc6.tom.inquiry.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    WAITING("대기 중"), ANSWERED("답변 완료");

    private final String adminKor;
}
