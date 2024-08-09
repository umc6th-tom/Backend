package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.BoardPicture;

import java.util.List;

@Repository
public interface BoardPictureRepository extends JpaRepository<BoardPicture, Long> {

    @Modifying
    @Query("DELETE FROM BoardPicture bp WHERE bp.pic = :picUrl")
    void deleteByPicUrl(String picUrl);

    List<BoardPicture> findAllByBoardId(Long boardId);
}
