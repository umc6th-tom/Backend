package umc6.tom.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.alarm.model.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
