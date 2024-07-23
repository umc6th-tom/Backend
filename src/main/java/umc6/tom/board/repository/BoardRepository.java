package umc6.tom.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.enums.Status;
import umc6.tom.user.model.User;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByStatusAndMajorsOrderByCreatedAtDesc(Status status, Majors majors, PageRequest pageRequest);

    Page<Board> findAllByStatusOrderByCreatedAtDesc(Status status, PageRequest pageRequest);

    Page<Board> findAllByTitleContainingOrderByCreatedAtDesc(String title, PageRequest pageRequest);

    Page<Board> findAllByContentContainingOrderByCreatedAtDesc(String content, PageRequest pageRequest);

    Page<Board> findAllByUser_NickNameContainingOrderByCreatedAtDesc(String nickName, PageRequest pageRequest);

    Page<Board> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content, PageRequest pageRequest);

    Page<Board> findAllByUserIdOrderByCreatedAtDesc(Long userId, PageRequest pageRequest);


}
