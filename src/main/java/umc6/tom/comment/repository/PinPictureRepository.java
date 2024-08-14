package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;

import java.util.List;

@Repository
public interface PinPictureRepository extends JpaRepository<PinPicture, Long> {

    @Modifying
    @Query("DELETE FROM PinPicture pp WHERE pp.pin = :pin")
    void deleteAllByPin(Pin pin);

    List<PinPicture> findAllByPinId(Long id);

    void deleteByPic(String pic);

    void deleteAllByPinId(Long id);

}
