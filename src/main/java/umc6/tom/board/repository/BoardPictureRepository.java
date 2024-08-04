package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.BoardPicture;

@Repository
public interface BoardPictureRepository extends JpaRepository<BoardPicture, Long> {

    void deleteAllByBoardId(Long boardId);

    @Modifying
    @Query("DELETE FROM BoardPicture bp WHERE bp.pic = :picUrl")
    void deleteByPicUrl(String picUrl);
}
