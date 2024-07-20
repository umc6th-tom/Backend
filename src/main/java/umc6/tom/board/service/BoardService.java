package umc6.tom.board.service;

import org.springframework.data.domain.Page;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;

public interface BoardService {
    Board registerBoard(BoardRequestDto.RegisterDto request, Long userId);

    Page<Board> getBoardList(Long boardId, Integer page);

}
