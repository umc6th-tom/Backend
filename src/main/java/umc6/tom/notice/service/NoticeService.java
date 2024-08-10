package umc6.tom.notice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.notice.converter.NoticeConverter;
import umc6.tom.notice.dto.NoticeDto;
import umc6.tom.notice.dto.NoticePictureDto;
import umc6.tom.notice.dto.NoticeReq;
import umc6.tom.notice.model.Notice;
import umc6.tom.notice.model.NoticePicture;
import umc6.tom.notice.repository.NoticePictureRepository;
import umc6.tom.notice.repository.NoticeRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticePictureRepository noticePictureRepository;
    private final UserRepository userRepository;

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
                .map(notice -> NoticeDto.toNoticeDTO(notice))
                .orElse(null);
    }

    //공지사항 등록
    public boolean register(NoticeReq.registerDto registerDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Notice notice = noticeRepository.save(NoticeConverter.toNoticeRegitser(registerDto, user));

        if(notice.getId() != null){
            return true;
        } else{
            return false;
        }
    }

    //공지사항 삭제
    public boolean delete(Long noticeId) {
        noticeRepository.deleteById(noticeId);

        boolean exists = noticeRepository.existsById(noticeId);

        if (!exists) {
            System.out.println("Notice 삭제 성공 ID: " + noticeId);
            return true;
        } else {
            System.err.println("Notice 삭제 실패 ID: " + noticeId);
            return false;
        }
    }

    public boolean update(Long noticeId, NoticeReq.registerDto req,Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 기존 Notice를 가져옴
        Notice existingNotice = noticeRepository.findAllById(noticeId);

        // 필요한 필드만 업데이트
        existingNotice.setTitle(req.getTitle());
        existingNotice.setContent(req.getContent());
        existingNotice.setUser(user);

        Notice notice = noticeRepository.save(existingNotice);

        if(Objects.equals(notice.getContent(), req.getContent()) && Objects.equals(notice.getTitle(), req.getTitle())){
            return true;
        }else{
            return false;
        }
    }
}
