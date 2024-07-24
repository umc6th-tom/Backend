package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.common.model.Majors;

public interface MajorRepositoryBoard extends JpaRepository<Majors, Long> {
}
