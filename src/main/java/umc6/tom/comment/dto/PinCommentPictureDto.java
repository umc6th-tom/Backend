package umc6.tom.comment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinCommentPictureDto {
    private Long id;
    private Long pinCommentId;
    private String pic;
}
