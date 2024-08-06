package umc6.tom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.mapping.ProhibitBoard;

public interface ProhibitBoardRepository extends JpaRepository<ProhibitBoard, Long> {
}
