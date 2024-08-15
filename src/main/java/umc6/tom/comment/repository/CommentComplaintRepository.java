package umc6.tom.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComplaint;

import java.util.List;

@Repository
public interface CommentComplaintRepository extends JpaRepository<CommentComplaint, Long> {
    List<CommentComplaint> findAllByCommentUserId(Long id);

    List<CommentComplaint> findAllByCommentUserIdOrderByCreatedAtDesc(Long writeUserId);

    List<CommentComplaint> findAllByCommentIdOrderByCreatedAtDesc(Long id);

    List<CommentComplaint> findAllByOrderByCreatedAtDesc();

    List<CommentComplaint> findAllByComment(Comment comment);
}
