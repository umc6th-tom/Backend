package umc6.tom.fAQ.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.fAQ.model.FAQ;
import umc6.tom.fAQ.model.enums.Category;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findAllByOrderByUpdatedAtDesc(PageRequest pageRequest);
    Page<FAQ> findAllByCategoryOrderByUpdatedAtDesc(Category category, PageRequest pageRequest);

    Page<FAQ> findAllByTitleContainingOrContentContainingOrderByUpdatedAtDesc(String title, String content, PageRequest pageRequest);

    @Query("SELECT faq FROM FAQ faq WHERE faq.category = :category AND (faq.title LIKE %:title% OR faq.content LIKE %:content%) ORDER BY faq.createdAt DESC")
    Page<FAQ> findAllByCategoryAndTitleContainingOrContentContainingOrderByUpdatedAtDesc(Category category,String title, String content, PageRequest pageRequest);


}
