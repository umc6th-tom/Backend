package umc6.tom.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.pin.model.PinCommentPicture;
@Repository
public interface PinCommentPictureRepository extends JpaRepository<PinCommentPicture, Long> {
}
