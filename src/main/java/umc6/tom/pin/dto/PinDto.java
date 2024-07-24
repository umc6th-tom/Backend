package umc6.tom.pin.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PinDto {
    public Long id;
    public Long userId;
    public Long boardId;
    public String comment;
    public Date createTime;
    public Date updateTime;


}
