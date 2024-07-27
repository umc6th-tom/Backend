package umc6.tom.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PinReportReqDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class PinReportDto{
        private String content;
        private List<String> pic;
    }

}
