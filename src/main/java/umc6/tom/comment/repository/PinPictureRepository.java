package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;
@Repository
public interface PinPictureRepository extends JpaRepository<PinPicture, Long> {
    void deleteAllByPin(Pin pin);
}
