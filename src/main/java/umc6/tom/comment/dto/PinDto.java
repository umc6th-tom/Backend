package umc6.tom.comment.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinDto {
    private Long id;
    private Long userId;
    private Long boardId;
    private String comment;
    private Date createTime;
    private Date updateTime;
}
