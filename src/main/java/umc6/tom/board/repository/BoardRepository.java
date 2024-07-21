package umc6.tom.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.common.model.Majors;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByMajorsOrderByCreatedAtDesc(Majors majors, PageRequest pageRequest);

    Page<Board> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

}
