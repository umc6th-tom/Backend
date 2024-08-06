package umc6.tom.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.inquiry.model.InquiryPicture;

public interface InquiryPictureRepository extends JpaRepository<InquiryPicture, Long> {
}
