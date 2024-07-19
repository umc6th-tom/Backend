package umc6.tom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.Resign;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResignRepository extends JpaRepository<Resign, Long> {

    Optional<Resign> findByUser(User user);

    List<Resign> findByTimerBefore(LocalDateTime timer);
}
