package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.comment.model.PinComplaint;

@Repository
public interface CommentComplaintRepository extends JpaRepository<CommentComplaint, Long> {
}
