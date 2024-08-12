package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentPicture;
import umc6.tom.comment.model.Pin;

import java.util.List;

@Repository
public interface CommentPictureRepository extends JpaRepository<CommentPicture, Long> {

    @Modifying
    @Query("DELETE FROM CommentPicture pp WHERE pp.comment = :comment")
    void deleteAllByComment(Comment comment);

    List<CommentPicture> findAllByCommentId(Long id);

    void deleteByCommentId(Long user);

}
