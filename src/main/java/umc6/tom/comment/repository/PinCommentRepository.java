package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.PinComment;
@Repository
public interface PinCommentRepository extends JpaRepository<PinComment, Long> {
}
