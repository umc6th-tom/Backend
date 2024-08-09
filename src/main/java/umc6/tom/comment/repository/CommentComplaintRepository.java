package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.comment.model.PinComplaint;

import java.util.List;

@Repository
public interface CommentComplaintRepository extends JpaRepository<CommentComplaint, Long> {
    List<CommentComplaint> findAllByCommentUserId(Long id);
}
