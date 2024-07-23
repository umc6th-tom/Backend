package umc6.tom.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.user.model.User;

import java.util.Optional;

public interface AlarmSetRepository extends JpaRepository<AlarmSet, Long> {

    Optional<AlarmSet> findByUser(User user);
}
