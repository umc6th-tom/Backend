package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.BoardPicture;

public interface BoardPictureRepository extends JpaRepository<BoardPicture, Long> {
}
