package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.CommentLike;
import umc6.tom.comment.model.PinLike;
import umc6.tom.user.model.User;

@Repository
public interface PinLikeRepository extends JpaRepository<PinLike, Long> {
    PinLike findByUser(User user);
}
