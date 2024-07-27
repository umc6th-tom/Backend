package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentPicture;
import umc6.tom.comment.model.Pin;

@Repository
public interface CommentPictureRepository extends JpaRepository<CommentPicture, Long> {
    void deleteAllByComment(Comment comment);
}
