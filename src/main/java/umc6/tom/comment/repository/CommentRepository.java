package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
