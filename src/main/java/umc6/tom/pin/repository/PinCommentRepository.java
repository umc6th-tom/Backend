package umc6.tom.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.pin.model.PinComment;
@Repository
public interface PinCommentRepository extends JpaRepository<PinComment, Long> {
}
