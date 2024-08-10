package umc6.tom.comment.dto;

import lombok.*;

import java.util.List;


public class PinPictureDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class PinPictureDto2 {
        private Long id;
        private Long pinId;
        private List<String> pic;
    }


}