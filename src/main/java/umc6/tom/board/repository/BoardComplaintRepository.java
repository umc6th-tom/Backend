package umc6.tom.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.BoardComplaint;

import java.util.List;

public interface BoardComplaintRepository extends JpaRepository<BoardComplaint, Long> {
    List<BoardComplaint> findAllByBoardUserId(Long userId);
}
