package umc6.tom.comment.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinLikeDto {
    private Long id;
    private Long userId;
    private Long pinId;
}
