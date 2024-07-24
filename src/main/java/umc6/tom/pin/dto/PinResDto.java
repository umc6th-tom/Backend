package umc6.tom.pin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PinResDto {
    private Long id;
    private Long userId;
    private Long boardId;
    private String comment;
    private List<String> pic;
}
