package umc6.tom.comment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentPictureDto {
    private Long id;
    private Long commentId;
    private String pic;
}
