package umc6.tom.comment.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinPictureDto {
    private Long id;
    private Long pinId;
    private List<String> pic;
}
