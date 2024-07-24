package umc6.tom.notice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.notice.model.NoticePicture;

import java.util.List;

public interface NoticePictureRepository extends JpaRepository<NoticePicture, Long> {
    List<NoticePicture> findByNoticeId(Long noticeId);
}
