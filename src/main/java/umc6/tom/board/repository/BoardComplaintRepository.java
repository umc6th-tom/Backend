package umc6.tom.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.board.model.BoardComplaint;

public interface BoardComplaintRepository extends JpaRepository<BoardComplaint, Long> {
}
