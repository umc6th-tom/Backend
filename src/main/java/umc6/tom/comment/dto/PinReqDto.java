package umc6.tom.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PinReqDto {
    private String comment;
    private List<String> pic;
}
