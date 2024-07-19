package umc6.tom.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import umc6.tom.notice.model.Notice;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    private Long id;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    //공지사항 리스트 조회 content 제외
    public static NoticeDto toNoticeListDTO(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setUpdatedAt(notice.getUpdatedAt());

        return dto;
    }

    //공지사항 글 조회 content 추가
    public static NoticeDto toNoticeDTO(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setUpdatedAt(notice.getUpdatedAt());

        return dto;
    }
}
