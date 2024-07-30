package umc6.tom.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResponseDto.RegisterResultDto> join(@RequestPart @Valid BoardRequestDto.RegisterDto request,
                                                                @RequestPart MultipartFile[] files) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Board board = boardService.registerBoard(request, userId, files);
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
    public ApiResponse<BoardResponseDto.BoardComplaintDto> reportBoard(@RequestBody @Valid BoardRequestDto.AddComplaintDto request,
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

    /**
     * 24.07.23 작성자 : 박재락
     * 전공 게시판 검색 조회
     */
    @GetMapping("/search-major/{major-id}/{search_type}/{search_keyWord}")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> searchMajorBoard(@PathVariable (name = "major-id") Long majorId,
                                 @PathVariable (name = "search_type") String searchType,
                                 @PathVariable (name = "search_keyWord") String searchKeyword,
                                 @RequestParam(name = "page") Integer page) {
        Page<Board> boardPage = boardService.getSearchMajorBoardList(majorId, searchType, searchKeyword, page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));

    }

    /**
     * 24.07.25 작성자 : 박재락
     * 핫한 게시판 검색 조회
     */
    @GetMapping("/search-hot/{search_type}/{search_keyWord}")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> searchHotBoard(@PathVariable (name = "search_type") String searchType,
                                                                             @PathVariable (name = "search_keyWord") String searchKeyword,
                                                                             @RequestParam(name = "page") Integer page) {

        Page<Board> boardPage = boardService.getSearchHotBoardList(searchType, searchKeyword, page);
        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }

    /**
     * 24.07.24 작성자 : 박재락
     * 핫한 게시판 조회
     */
    @GetMapping("/hot/{major_id}")
    public ApiResponse<BoardResponseDto.BoardListViewListDto> hotBoardList(@PathVariable (name = "major_id") Long majorId,
                                                                           @RequestParam(name = "page") Integer page) {
        Page<Board> boardPage = boardService.getBoardHotList(page, majorId);

        return ApiResponse.onSuccess(BoardConverter.toBoardListViewListDTO(boardPage));
    }

    /**
     * 24.07.24 작성자 : 박재락
     * 메인 게시판 홈 조회
     */
    @GetMapping("/main")
    public ApiResponse<BoardResponseDto.BoardMainViewListDto> mainBoardList() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(boardService.getBoardMainList(userId));
    }

    /**
     * 24.07.24 작성자 : 박재락
     * 게시물 수정
     */
    @PatchMapping(value = "/{board_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BoardResponseDto.BoardUpdateDto> updateBoard(@RequestPart @Valid BoardRequestDto.UpdateBoardDto request,
                                                                    @RequestPart MultipartFile[] files,
                                                                    @PathVariable(name = "board_id") Long boardId){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        Board board = boardService.updateBoard(request, userId, boardId, files);

        return ApiResponse.onSuccess(BoardConverter.toUpdateBoardDto(board));
    }

    /**
     * 24.07.25 작성자 : 박재락
     * 게시글 조회
     */
    @GetMapping("/{board_id}")
    public ApiResponse<BoardResponseDto.BoardViewDto> boardView(@PathVariable(name = "board_id") Long boardId) {

        return ApiResponse.onSuccess(boardService.getBoardView(boardId));
    }

}
