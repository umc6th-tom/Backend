package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.PinComment;
import umc6.tom.comment.model.PinLike;

@Repository
public interface PinCommentLikeRepository extends JpaRepository<PinLike, Long> {
}
