package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComment;

import java.util.List;

public interface PinCommentRepositoryBoard extends JpaRepository<PinComment, Long> {

    List<PinComment> findAllByPin(Pin pin);
}
