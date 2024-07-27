package umc6.tom.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CommentResDto {
    private Long id;
    private Long userId;
    private Long pinId;
    private String comment;
    private List<String> pic;
}
