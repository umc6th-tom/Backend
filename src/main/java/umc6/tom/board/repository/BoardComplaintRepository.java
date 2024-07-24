package umc6.tom.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.user.model.User;

public interface BoardComplaintRepository extends JpaRepository<BoardComplaint, Long> {
}
