package umc6.tom.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.user.model.User;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsBoardLikeByUserAndBoard(User user, Board board);

    void deleteBoardLikeByUserAndBoard(User user, Board board);

    List<BoardLike> findAllByUserIdOrderByIdDesc(Long userId);

    Page<BoardLike> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}
