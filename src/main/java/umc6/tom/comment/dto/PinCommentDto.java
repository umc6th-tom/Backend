package umc6.tom.comment.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinCommentDto {
    private Long id;
    private Long userId;
    private Long pinId;
    private String comment;
    private Date createTime;
    private Date updateTime;
}
