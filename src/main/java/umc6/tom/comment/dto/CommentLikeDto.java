package umc6.tom.comment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeDto {
    private Long id;
    private Long userId;
    private Long commentId;
}
