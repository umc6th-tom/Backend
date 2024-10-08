package umc6.tom.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.notice.model.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findAllById(Long id);
}
