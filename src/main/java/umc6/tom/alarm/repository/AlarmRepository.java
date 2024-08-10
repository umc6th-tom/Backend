package umc6.tom.alarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Category;
import umc6.tom.alarm.model.enums.IsRead;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, IsRead isRead, PageRequest pageRequest);
    Page<Alarm> findAllByUserIdAndIsReadAndCategoryOrderByCreatedAtDesc(Long userId, IsRead isRead, Category category, PageRequest pageRequest);

    boolean existsByCategoryAndAlarmContainingAndBoardId(Category category, String alarm, Long boardId);
}
