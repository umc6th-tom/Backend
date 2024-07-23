package umc6.tom.board.converter;

import org.springframework.data.domain.Page;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.comment.model.Pin;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class BoardConverter {


    public static Board toBoard(BoardRequestDto.RegisterDto request){

        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public static BoardResponseDto.RegisterResultDto toRegisterResultDto(Board board){
        return BoardResponseDto.RegisterResultDto.builder()
                .boardId(board.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BoardResponseDto.BoardListViewDto toBoardListViewDto(Board board){
        int pinCommentSize = 0; //대댓글 개수
        for (Pin pin : board.getPinList())
            pinCommentSize += pin.getPinCommentList().size();

        return BoardResponseDto.BoardListViewDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .likeCount(board.getBoardLikeList().size())
                .pinCount(board.getPinList().size() + pinCommentSize)
                .userNickName(board.getUser().getNickName())
                .boardDate(new DateCalc().boardListDate(board.getUpdatedAt()))
                .build();
    }

    public static BoardResponseDto.BoardListViewListDto toBoardListViewListDTO(Page<Board> boardList){
        List<BoardResponseDto.BoardListViewDto> boardListViewDtoList = boardList.stream()
                .map(BoardConverter::toBoardListViewDto).collect(Collectors.toList());

        return BoardResponseDto.BoardListViewListDto.builder()
                .isLast(boardList.isLast())
                .isFirst(boardList.isFirst())
                .totalPage(boardList.getTotalPages())
                .totalElements(boardList.getTotalElements())
                .listSize(boardListViewDtoList.size())
                .boardList(boardListViewDtoList)
                .build();
    }

    public static BoardLike toBoardLike(User user, Board board) {
        return BoardLike.builder()
                .board(board)
                .user(user)
                .build();
    }

    public static BoardResponseDto.BoardLikeAddDto toAddBoardLike(BoardLike boardLike){
        return BoardResponseDto.BoardLikeAddDto.builder()
                .boardId(boardLike.getBoard().getId())
                .userId(boardLike.getUser().getId())
                .build();
    }

    public static BoardResponseDto.BoardDeleteDto toDeleteResultDto(Board board){
        return BoardResponseDto.BoardDeleteDto.builder()
                .boardId(board.getId())
                .build();
    }

    public static BoardComplaint toBoardComplaint(BoardRequestDto.AddComplaintDto request, User user, Board board) {
        return BoardComplaint.builder()
                .board(board)
                .user(user)
                .content(request.getContent())
                .build();
    }

    public static BoardResponseDto.BoardComplaintDto toBoardComplaintResultDto(BoardComplaint boardComplaint){
        return BoardResponseDto.BoardComplaintDto.builder()
                .boardId(boardComplaint.getId())
                .userId(boardComplaint.getUser().getId())
                .build();
    }

}
