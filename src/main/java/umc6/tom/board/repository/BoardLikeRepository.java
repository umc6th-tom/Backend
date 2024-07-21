package umc6.tom.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.user.model.User;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsBoardLikeByUserAndBoard(User user, Board board);

    void deleteBoardLikeByUserAndBoard(User user, Board board);

}
