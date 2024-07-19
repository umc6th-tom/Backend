package umc6.tom.notice.dto;

import lombok.*;
import umc6.tom.notice.model.NoticePicture;

import java.util.List;

@Getter
@Setter
@ToString //필드값학인
@NoArgsConstructor //기본생성자
@AllArgsConstructor
public class NoticePictureDto {
    private Long id;
    private String pic;

    //공지사항 리스트 조회 content 제외
    public static NoticePictureDto toNoticeListDTO(NoticePicture noticePicture) {
        NoticePictureDto dto = new NoticePictureDto();
        dto.setId(noticePicture.getId());
        dto.setPic(noticePicture.getPic());

        return dto;
    }

}
