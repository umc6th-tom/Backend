package umc6.tom.notice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc6.tom.notice.dto.NoticeDto;
import umc6.tom.notice.dto.NoticePictureDto;
import umc6.tom.notice.model.Notice;
import umc6.tom.notice.model.NoticePicture;
import umc6.tom.notice.repository.NoticePictureRepository;
import umc6.tom.notice.repository.NoticeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticePictureRepository noticePictureRepository;

    //공지사항 리스트 조회
    @Transactional
    public List<NoticeDto> findAll(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<NoticeDto> noticeDTOList = noticePage.stream()
                .map(NoticeDto::toNoticeListDTO)
                .collect(Collectors.toList());

        return noticeDTOList;
    }

    //공지사항 글 보기
    @Transactional
    public NoticeDto findNotice(long id) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        List<NoticePicture> pictures = noticePictureRepository.findByNoticeId(id);

        List<NoticePictureDto> pictureDtos = pictures.stream()
                                                        .map(NoticePictureDto::toNoticeListDTO)
                                                        .collect(Collectors.toList());

        return optionalNotice
                .map(notice -> NoticeDto.toNoticeDTO(notice, pictureDtos))
                .orElse(null);
    }
}
