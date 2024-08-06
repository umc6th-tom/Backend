package umc6.tom.alarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Field;
import umc6.tom.alarm.model.enums.IsRead;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, IsRead isRead, PageRequest pageRequest);
    Page<Alarm> findAllByUserIdAndIsReadAndCategoryOrderByCreatedAtDesc(Long userId, IsRead isRead, Field field, PageRequest pageRequest);
}
