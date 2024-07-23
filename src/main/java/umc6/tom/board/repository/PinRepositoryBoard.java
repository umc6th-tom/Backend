package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.Pin;

public interface PinRepositoryBoard extends JpaRepository<Pin, Long> {
}
