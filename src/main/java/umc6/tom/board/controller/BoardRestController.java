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
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.model.BoardLike;
import umc6.tom.board.service.BoardService;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/board")
public class BoardRestController {
    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 24.07.19 작성자 : 박재락
     * 게시물 등록
     */
    //사진 등록구현 필요
    @PostMapping("/register")
    public ApiResponse<BoardResponseDto.RegisterResultDto> join(@RequestBody @Valid BoardRequestDto.RegisterDto request) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Board board = boardService.registerBoard(request, userId);
        return ApiResponse.onSuccess(BoardConverter.toRegisterResultDto(board));
    }

    /**
     * 24.07.20 작성자 : 박재락
     * 전공 게시물 리스트 조회
     */
    //사진 미리보기 구현 필요
    @GetMapping("/{majorId}/major")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> getBoardMajorList(@PathVariable(name = "majorId") Long majorId,
                                                                        @RequestParam(name = "page") Integer page){
        Page<Board> boardPage = boardService.getBoardMajorList(majorId, page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }

    /**
     * 24.07.21 작성자 : 박재락
     * 모든 게시물 리스트 조회
     */
    //사진 미리보기 구현 필요
    @GetMapping("/list")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> getBoardAllList(@RequestParam(name = "page") Integer page){
        Page<Board> boardPage = boardService.getBoardAllList(page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }

    /**
     * 24.07.21 작성자 : 박재락
     * 게시물 좋아요 추가
     */
    @PostMapping("/like/{board_id}")
    public ApiResponse<BoardResponseDto.BoardLikeAddDto> addBoardLike(@PathVariable (name = "board_id") Long boardId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        BoardLike boardLike = boardService.addBoardLike(userId,boardId);
        return ApiResponse.onSuccess(BoardConverter.toAddBoardLike(boardLike));
    }

    /**
     * 24.07.21 작성자 : 박재락
     * 게시물 좋아요 삭제
     */
    @DeleteMapping("/like/{board_id}")
    public ApiResponse<BoardResponseDto.BoardLikeAddDto> deleteBoardLike(@PathVariable (name = "board_id") Long boardId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        BoardLike boardLike = boardService.deleteBoardLike(userId,boardId);
        return ApiResponse.onSuccess(BoardConverter.toAddBoardLike(boardLike));
    }

    /**
     * 24.07.22 작성자 : 박재락
     * 게시물 삭제 (나중에 다시 수정. 댓글 삭제해도 대댓글 남아 있으면 게시물 삭제 못하게 해야 함)
     */
    @DeleteMapping("/{board_id}")
    public ApiResponse<BoardResponseDto.BoardDeleteDto> deleteBoard(@PathVariable (name = "board_id") Long boardId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Board board = boardService.deleteBoard(userId, boardId);
        return ApiResponse.onSuccess(BoardConverter.toDeleteResultDto(board));
    }

    /**
     * 24.07.22 작성자 : 박재락
     * 게시물 신고
     */
    @PostMapping("/report/{board_id}")
    public ApiResponse<BoardResponseDto.BoardComplaintDto> deleteBoard(@RequestBody @Valid BoardRequestDto.AddComplaintDto request,
                                                                       @PathVariable (name = "board_id") Long boardId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        BoardComplaint boardComplaint= boardService.complaintBoard(request, userId, boardId);
        return ApiResponse.onSuccess(BoardConverter.toBoardComplaintResultDto(boardComplaint));
    }

    /**
     * 24.07.23 작성자 : 박재락
     * 전체 게시판 검색 조회
     */
    @GetMapping("/search-all/{search-type}/{search-keyWord}")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> searchAllBoard(@PathVariable (name = "search-type") String searchType,
                                                                             @PathVariable (name = "search-keyWord") String searchKeyword,
                                                                             @RequestParam(name = "page") Integer page) {

        Page<Board> boardPage = boardService.getSearchAllBoardList(searchType, searchKeyword, page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }

//    @GetMapping("/search-major/{major-id}/{search_type}/{search_keyWord}")
//    public void searchMajorBoard(@PathVariable (name = "major-id") String majorId,
//                                 @PathVariable (name = "search_type") String searchType,
//                                 @PathVariable (name = "search_keyWord") String searchKeyword,
//                                 @RequestParam(name = "page") Integer page) {
//
//    }
//
//    @GetMapping("/search-hot/{search_type}/{search_keyWord}")
//    public void searchHotBoard(@PathVariable (name = "search_type") String searchType,
//                               @PathVariable (name = "search_keyWord") String searchKeyword,
//                               @RequestParam(name = "page") Integer page) {
//
//    }
//
//    @GetMapping("/myboards")
//    public void myBoards(@RequestParam(name = "page") Integer page) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
//
//    }
}
