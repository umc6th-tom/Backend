package umc6.tom.board.converter;

import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import umc6.tom.board.model.*;
import umc6.tom.comment.model.Pin;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.comment.model.Comment;
import umc6.tom.comment.model.CommentPicture;
import umc6.tom.comment.model.PinPicture;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BoardConverter {


    public static Board toBoard(BoardRequestDto.RegisterDto request, User user, Majors majors){

        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .majors(majors)
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
            pinCommentSize += pin.getCommentList().size();
        String boardPreViewPic = null;

        //list는 없는 index 참조시 나는 오류를 제거
        if(!ObjectUtils.isEmpty(board.getBoardPictureList()))
            boardPreViewPic = board.getBoardPictureList().get(0).getPic();

        return BoardResponseDto.BoardListViewDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .likeCount(board.getBoardLikeList().size())
                .pinCount(board.getPinList().size() + pinCommentSize)
                .userNickName(board.getUser().getNickName())
                .picPreview(boardPreViewPic)
                .boardDate(new DateCalc().boardListDate(board.getCreatedAt()))
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
                .boardTitle(board.getTitle())
                .boardContent(board.getContent())
                .boardUserId(board.getUser().getId())
                .complaintContent(request.getContent())
                .build();
    }

    public static BoardResponseDto.BoardComplaintDto toBoardComplaintResultDto(BoardComplaint boardComplaint){
        return BoardResponseDto.BoardComplaintDto.builder()
                .boardId(boardComplaint.getId())
                .userId(boardComplaint.getUser().getId())
                .build();
    }

    public static BoardResponseDto.BoardMainViewDto toBoardMainListViewDto(Board board){
        int pinCommentSize = 0; //대댓글 개수
        for (Pin pin : board.getPinList())
            pinCommentSize += pin.getCommentList().size();

        return BoardResponseDto.BoardMainViewDto.builder()
                .title(board.getTitle())
                .likeCount(board.getBoardLikeList().size())
                .pinCount(board.getPinList().size() + pinCommentSize)
                .isHavingPic(!ObjectUtils.isEmpty(board.getBoardPictureList()))
                .build();
    }

    public static BoardResponseDto.BoardMainViewListDto toBoardMainListViewListDto(List<Board> boardMajorList,
                                                                                   List<Board> boardHotList,
                                                                                   List<Board> boardAllList){

        List<BoardResponseDto.BoardMainViewDto> boardMajorListViewDtoList = boardMajorList.stream()
                .map(BoardConverter::toBoardMainListViewDto).collect(Collectors.toList());

        List<BoardResponseDto.BoardMainViewDto> boardHotListViewDtoList = boardHotList.stream()
                .map(BoardConverter::toBoardMainListViewDto).collect(Collectors.toList());

        List<BoardResponseDto.BoardMainViewDto> boardAllListViewDtoList = boardAllList.stream()
                .map(BoardConverter::toBoardMainListViewDto).collect(Collectors.toList());

        return BoardResponseDto.BoardMainViewListDto.builder()
                .boardMajorList(boardMajorListViewDtoList)
                .boardHotList(boardHotListViewDtoList)
                .boardAllList(boardAllListViewDtoList)
                .build();
    }
    public static BoardPicture toBoardPicture(Board board, String pic){
        return BoardPicture.builder()
                .board(board)
                .pic(pic)
                .build();
    }

    public static List<String> toStringList(List<BoardPicture> boardPictureList){
        List<String> picList = new ArrayList<>();
        for(BoardPicture boardPicture : boardPictureList){
            picList.add(boardPicture.getPic());
        }
        return picList;
    }

    public static BoardResponseDto.BoardUpdateDto toUpdateBoardDto(Board board){
        return BoardResponseDto.BoardUpdateDto.builder()
                .boardId(board.getId())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public static BoardResponseDto.BoardViewDto toBoardViewDto(Board board){
        int pinCommentSize = 0; //대댓글 개수
        for (Pin pin : board.getPinList())
            pinCommentSize += pin.getCommentList().size();

        List<BoardResponseDto.BoardViewPinListDto> toBoardViewPinListDtoList = board.getPinList().stream()
                .map(BoardConverter::toBoardViewPinListDto).collect(Collectors.toList());

        List<String> newBoardPicList = new ArrayList<>();
        for (BoardPicture picture : board.getBoardPictureList())
            newBoardPicList.add(picture.getPic());

        return BoardResponseDto.BoardViewDto.builder()
                .userId(board.getUser().getId())
                .userNickname(board.getUser().getNickName())
                .userProfilePic(board.getUser().getPic())
                .title(board.getTitle())
                .content(board.getContent())
                .pinCount(board.getPinList().size() + pinCommentSize)
                .likeCount(board.getBoardLikeList().size())
                .boardDate(new DateCalc().boardListDate(board.getCreatedAt()))
                .boardPic(newBoardPicList)
                .pinList(toBoardViewPinListDtoList)
                .build();
    }

    public static BoardResponseDto.BoardViewPinListDto toBoardViewPinListDto(Pin pin){
        int pinCommentSize = 0; //대댓글 개수
        for (Comment pinComment : pin.getCommentList())
            pinCommentSize += 1;

        List<BoardResponseDto.BoardViewPinCommentListDto> toBoardViewPinCommentListDtoList = pin.getCommentList().stream()
                .map(BoardConverter::toBoardPinCommentListDto).collect(Collectors.toList());

        List<String> newPinPicList = new ArrayList<>();
        for (PinPicture picture : pin.getPinPictureList())
            newPinPicList.add(picture.getPic());

        return BoardResponseDto.BoardViewPinListDto.builder()
                .id(pin.getId())
                .userId(pin.getUser().getId())
                .userNickname(pin.getUser().getNickName())
                .comment(pin.getComment())
                .pinDate(new DateCalc().boardListDate(pin.getCreatedAt()))
                .pinLikeCount(pin.getPinLikeList().size())
                .pinCommentCount(pinCommentSize)
                .pinCommentList(toBoardViewPinCommentListDtoList)
                .pinPictureList(newPinPicList)
                .build();
    }

    public static BoardComplaintPicture toBoardComplaintPictureDto(BoardComplaint boardComplaint, String boardPic){

        return BoardComplaintPicture.builder()
                .boardComplaint(boardComplaint)
                .pic(boardPic)
                .build();
    }

    public static BoardResponseDto.BoardViewPinCommentListDto toBoardPinCommentListDto(Comment comment){
        List<String> newPinCommentPicList = new ArrayList<>();

        for (CommentPicture picture : comment.getCommentPictureList())
            newPinCommentPicList.add(picture.getPic());

        return BoardResponseDto.BoardViewPinCommentListDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickName())
                .comment(comment.getComment())
                .pinCommentDate(new DateCalc().boardListDate(comment.getCreatedAt()))
                .pinLikeCount(comment.getCommentLikeList().size())
                .pinPicList(newPinCommentPicList)
                .build();
    }

    public static BoardResponseDto.FindProfileDto toFindProfileDto(Board board){
        return BoardResponseDto.FindProfileDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .pinCount(board.getPinList().size())
                .likeCount(board.getBoardLikeList().size())
                .build();
    }

    public static BoardResponseDto.FindUserBoardsDto toFindBoardsDto(Board board){
        return BoardResponseDto.FindUserBoardsDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(new DateCalc().formatDate(board.getCreatedAt()))
                .build();
    }

    public static BoardResponseDto.FindUserBoardsDto toFindCommentsDto(Pin pin, Board board){
        return BoardResponseDto.FindUserBoardsDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(new DateCalc().formatDate(pin.getCreatedAt()))
                .build();
    }
}
