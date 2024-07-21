package umc6.tom.board.service;

import org.springframework.data.domain.Page;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;

public interface BoardService {

    Board registerBoard(BoardRequestDto.RegisterDto request, Long userId);
    Page<Board> getBoardMajorList(Long boardId, Integer page);
    Page<Board> getBoardAllList(Integer page);

    BoardLike addBoardLike(Long userId, Long boardId);
    BoardLike deleteBoardLike(Long userId, Long boardId);
}
