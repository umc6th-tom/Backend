package umc6.tom.board.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.enums.BoardStatus;
import umc6.tom.common.model.Majors;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.popularAt = :now WHERE b.id = :boardId")
    void updateBoardPopularAt(Long boardId, LocalDateTime now);

    Page<Board> findAllByStatusAndMajorsOrderByCreatedAtDesc(BoardStatus status, Majors majors, PageRequest pageRequest);

    Page<Board> findAllByStatusOrderByCreatedAtDesc(BoardStatus status, PageRequest pageRequest);

    Page<Board> findAllByStatusAndTitleContainingOrderByCreatedAtDesc(BoardStatus status, String title, PageRequest pageRequest);

    Page<Board> findAllByStatusAndContentContainingOrderByCreatedAtDesc(BoardStatus status, String content, PageRequest pageRequest);

    Page<Board> findAllByStatusAndUser_NickNameContainingOrderByCreatedAtDesc(BoardStatus status, String nickName, PageRequest pageRequest);

    Page<Board> findAllByStatusAndTitleContainingOrContentContainingOrderByCreatedAtDesc
            (BoardStatus status, String title, String content, PageRequest pageRequest);

    Page<Board> findAllByStatusAndMajorsIdAndTitleContainingOrderByCreatedAtDesc
            (BoardStatus status, Long majorId, String title, PageRequest pageRequest);

    Page<Board> findAllByStatusAndMajorsIdAndContentContainingOrderByCreatedAtDesc
            (BoardStatus status, Long majorId, String title, PageRequest pageRequest);
    Page<Board> findAllByStatusAndMajorsIdAndTitleContainingOrContentContainingOrderByCreatedAtDesc
            (BoardStatus status, Long majorId, String title, String content, PageRequest pageRequest);
    Page<Board> findAllByStatusAndMajorsIdAndUser_NickNameContainingOrderByCreatedAtDesc
            (BoardStatus status, Long majorId, String nickName, PageRequest pageRequest);

    Page<Board> findAllByStatusAndTitleContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
            (BoardStatus status, String title, PageRequest pageRequest);
    Page<Board> findAllByStatusAndContentContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
            (BoardStatus status, String content, PageRequest pageRequest);
    Page<Board> findAllByStatusAndTitleContainingOrContentContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
            (BoardStatus status, String title, String content, PageRequest pageRequest);
    Page<Board> findAllByStatusAndUser_NickNameContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
            (BoardStatus status, String nickName, PageRequest pageRequest);

    Page<Board> findAllByStatusAndMajorsIdAndPopularAtIsNotNullOrderByCreatedAtDesc(BoardStatus status, Long majorId,PageRequest pageRequest);

    List<Board> findTop5ByStatusAndMajorsOrderByCreatedAtDesc(BoardStatus status,Majors majors);
    List<Board> findTop5ByStatusAndPopularAtNotNullOrderByCreatedAtDesc(BoardStatus status);
    List<Board> findTop5ByStatusOrderByCreatedAtDesc(BoardStatus status);

    List<Board> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);

    List<Board> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Board> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Board findAllById(Long id);

}
