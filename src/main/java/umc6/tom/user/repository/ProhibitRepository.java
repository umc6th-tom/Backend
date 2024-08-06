package umc6.tom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.Prohibit;

public interface ProhibitRepository extends JpaRepository<Prohibit, Long> {
}
