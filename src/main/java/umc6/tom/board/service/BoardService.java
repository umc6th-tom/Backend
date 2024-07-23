package umc6.tom.board.service;

import org.springframework.data.domain.Page;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.model.BoardLike;

public interface BoardService {

    Board registerBoard(BoardRequestDto.RegisterDto request, Long userId);
    Page<Board> getBoardMajorList(Long boardId, Integer page);
    Page<Board> getBoardAllList(Integer page);

    BoardLike addBoardLike(Long userId, Long boardId);
    BoardLike deleteBoardLike(Long userId, Long boardId);
    Board deleteBoard(Long userId, Long boardId);

    BoardComplaint complaintBoard(BoardRequestDto.AddComplaintDto request, Long userId, Long boardId);

    Page<Board> getSearchAllBoardList(String searchType, String searchKeyword, Integer page);

    Page<Board> getMyBoardList(Long userId, Integer page);
}
