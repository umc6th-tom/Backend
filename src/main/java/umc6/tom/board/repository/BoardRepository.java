package umc6.tom.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.enums.Status;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByStatusAndMajorsOrderByCreatedAtDesc(Status status, Majors majors, PageRequest pageRequest);

    Page<Board> findAllByStatusOrderByCreatedAtDesc(Status status, PageRequest pageRequest);

    Page<Board> findAllByTitleContainingOrderByCreatedAtDesc(String title, PageRequest pageRequest);

    Page<Board> findAllByContentContainingOrderByCreatedAtDesc(String content, PageRequest pageRequest);

    Page<Board> findAllByUser_NickNameContainingOrderByCreatedAtDesc(String nickName, PageRequest pageRequest);

    Page<Board> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc
            (String title, String content, PageRequest pageRequest);

    Page<Board> findAllByMajorsIdAndTitleContainingOrderByCreatedAtDesc
            (Long majorId, String title, PageRequest pageRequest);

    Page<Board> findAllByMajorsIdAndContentContainingOrderByCreatedAtDesc
            (Long majorId, String title, PageRequest pageRequest);
    Page<Board> findAllByMajorsIdAndTitleContainingOrContentContainingOrderByCreatedAtDesc
            (Long majorId, String title, String content, PageRequest pageRequest);
    Page<Board> findAllByMajorsIdAndUser_NickNameContainingOrderByCreatedAtDesc
            (Long majorId, String nickName, PageRequest pageRequest);

    Page<Board> findAllByPopularAtIsNotNullOrderByCreatedAtDesc(PageRequest pageRequest);

    List<Board> findTop5ByMajorsOrderByCreatedAtDesc(Majors majors);
    List<Board> findTop5ByPopularAtIsNotNullOrderByCreatedAtDesc();
    List<Board> findTop5ByOrderByCreatedAtDesc();


}
