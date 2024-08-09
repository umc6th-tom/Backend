package umc6.tom.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.inquiry.model.Inquiry;
import umc6.tom.inquiry.model.enums.Status;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findAllByUserIdOrderByCreatedAtDesc(Long userId , PageRequest pageRequest);

    Page<Inquiry> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    Page<Inquiry> findAllByStatusOrderByCreatedAtDesc(Status status, PageRequest pageRequest);

    Page<Inquiry> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc
            (String title, String content, PageRequest pageRequest);

    @Query("SELECT inq FROM Inquiry inq WHERE inq.status = :status AND (inq.title LIKE %:title% OR inq.content LIKE %:content%) ORDER BY inq.createdAt DESC")
    Page<Inquiry> findAllByStatusContentTitle(Status status, String content, String title, PageRequest pageRequest);
}
