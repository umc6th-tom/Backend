package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.comment.model.Comment;

public interface PinCommentRepositoryBoard extends JpaRepository<Comment, Long> {
}
