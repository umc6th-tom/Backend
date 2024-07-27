package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.PinComplaint;
import umc6.tom.comment.model.PinComplaintPicture;

@Repository
public interface PinComplaintPictureRepository extends JpaRepository<PinComplaintPicture, Long> {
}
