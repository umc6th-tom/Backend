package umc6.tom.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.inquiry.model.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findAllByUserIdOrderByCreatedAtDesc(Long userId , PageRequest pageRequest);
}
