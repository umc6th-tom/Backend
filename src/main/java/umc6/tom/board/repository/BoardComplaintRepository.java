package umc6.tom.board.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.comment.model.CommentComplaint;
import umc6.tom.user.dto.UserDtoRes;

import java.util.List;

public interface BoardComplaintRepository extends JpaRepository<BoardComplaint, Long> {
    List<BoardComplaint> findAllByBoardUserId(Long userId);

    Page<BoardComplaint> findAllByBoardUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<BoardComplaint> findAllByBoardOrderByCreatedAtDesc(Board board);

    List<BoardComplaint> findAllByOrderByCreatedAtDesc();

    List<BoardComplaint> findAllByBoard(Board board);

//    @Query("SELECT new umc6.tom.user.dto.UserDtoRes.complaintAllResDto2('게시물', b.id, b.boardTitle, "
//            + "b.createdAt ,b.status) " +
//            "FROM BoardComplaint b " +
//            "ORDER BY b.createdAt DESC")
//    Page<UserDtoRes.complaintAllResDto> findAllBoardComplaints(Pageable pageable);

}
