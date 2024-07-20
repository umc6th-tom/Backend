package umc6.tom.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.service.BoardService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/board")
public class BoardRestController {
    private final BoardService boardService;

    /**
     * 24.07.19 작성자 : 박재락
     * 게시물 등록
     */
    //사진 등록구현 필요
    @PostMapping("/register")
    public ApiResponse<BoardResponseDto.RegisterResultDto> join(@RequestBody @Valid BoardRequestDto.RegisterDto request,
                                                                @RequestParam (name = "userId") Long userId) {
        Board board = boardService.registerBoard(request, userId);
        return ApiResponse.onSuccess(BoardConverter.toRegisterResultDto(board));
    }

    /**
     * 24.07.20 작성자 : 박재락
     * 전공 게시물 리스트 조회
     */
    //사진 미리보기 구현 필요
    @GetMapping("/{majorId}/major")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> getReviewList(@PathVariable(name = "majorId") Long majorId,
                                                                        @RequestParam(name = "page") Integer page){
        Page<Board> boardPage = boardService.getBoardList(majorId, page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }
}
