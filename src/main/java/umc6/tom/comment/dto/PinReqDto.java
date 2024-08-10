package umc6.tom.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class PinReqDto {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class PinCommentAndPic{
        private String comment;
        private List<String> pic;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class PinAndPic{
        private Long id;
        private String comment;
        private List<String> pic;
    }


}
