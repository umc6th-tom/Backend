package umc6.tom.fAQ.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.fAQ.model.FAQ;
import umc6.tom.fAQ.model.enums.Category;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findByUserIdOrderByUpdatedAtDesc(Long userId, PageRequest pageRequest);
    Page<FAQ> findByUserIdAndCategoryOrderByUpdatedAtDesc(Long userId, Category category, PageRequest pageRequest);
}
