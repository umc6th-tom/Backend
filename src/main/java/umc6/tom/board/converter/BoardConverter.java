package umc6.tom.board.converter;

import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.board.model.*;
import umc6.tom.comment.model.*;
import umc6.tom.comment.model.enums.PinBoardStatus;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BoardConverter {

    private static final String DEFAULT_PROFILE_PATH = "https://yesol.s3.ap-northeast-2.amazonaws.com/profile/defaultProfile.png";


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
                .id(board.getId())
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
                .boardComplaintId(boardComplaint.getId())
                .userId(boardComplaint.getUser().getId())
                .build();
    }

    public static BoardResponseDto.BoardMainViewDto toBoardMainListViewDto(Board board){
        int pinCommentSize = 0; //대댓글 개수
        for (Pin pin : board.getPinList())
            pinCommentSize += pin.getCommentList().size();

        return BoardResponseDto.BoardMainViewDto.builder()
                .id(board.getId())
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

    public static List<String> toPicStringIdList(List<BoardPicture> boardPictureList){
        List<String> picList = new ArrayList<>();
        for(BoardPicture boardPicture : boardPictureList){
            picList.add(boardPicture.getPic());
        }
        return picList;
    }
    public static List<String> toPicStringList(List<BoardPicture> boardPictureList){
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

    public static BoardResponseDto.BoardViewDto toBoardViewDto(Board board, Page<Pin> pinPage, Long userId){
        int pinCommentSize = 0; //대댓글 개수
        for (Pin pin : board.getPinList())
            pinCommentSize += pin.getCommentList().size();

        List<BoardResponseDto.BoardViewPinListDto> toBoardViewPinListDtoList = pinPage.stream()
                .map(pin -> BoardConverter.toBoardViewPinListDto(pin, userId)).collect(Collectors.toList());

        List<String> newBoardPicList = new ArrayList<>();
        for (BoardPicture picture : board.getBoardPictureList())
            newBoardPicList.add(picture.getPic());

        //게시글 보는 유저가 좋아요 눌렀는지 확인
        List<BoardLike> userLiked = board.getBoardLikeList().stream()
                .filter(user -> user.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        return BoardResponseDto.BoardViewDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .userNickname(board.getUser().getNickName())
                .userProfilePic(board.getUser().getPic())
                .title(board.getTitle())
                .content(board.getContent())
                .pinCount(board.getPinList().size() + pinCommentSize)
                .likeCount(board.getBoardLikeList().size())
                .boardDate(new DateCalc().boardListDate(board.getCreatedAt()))
                .isLiked(!ObjectUtils.isEmpty(userLiked))
                .boardPic(newBoardPicList)
                .pinList(toBoardViewPinListDtoList)
                .build();
    }

    public static BoardResponseDto.BoardViewPinListDto toBoardViewPinListDto(Pin pin, Long userId){
        int pinCommentSize = 0; //대댓글 개수
        for (Comment pinComment : pin.getCommentList())
            pinCommentSize += 1;

        List<BoardResponseDto.BoardViewPinCommentListDto> toBoardViewPinCommentListDtoList = pin.getCommentList().stream()
                .map(comment -> BoardConverter.toBoardPinCommentListDto(comment, userId)).collect(Collectors.toList());

        List<String> newPinPicList = new ArrayList<>();
        for (PinPicture picture : pin.getPinPictureList())
                newPinPicList.add(picture.getPic());

        String nickname = pin.getUser().getNickName();
        String comment = pin.getComment();
        String userPic = pin.getUser().getPic();
        int pinLike = pin.getPinLikeList().size();
        if(pin.getStatus().equals(PinBoardStatus.INACTIVE)){
            nickname = "알 수 없음";
            comment = "삭제된 댓글 입니다.";
            userPic = DEFAULT_PROFILE_PATH;
            pinLike = 0;
            pinCommentSize = 0;
        }

        //댓글 보는 유저가 좋아요 눌렀는지 확인
        List<PinLike> pinLiked = pin.getPinLikeList().stream()
                .filter(user -> user.getUser().getId().equals(userId))
                .toList();

        return BoardResponseDto.BoardViewPinListDto.builder()
                .id(pin.getId())
                .userId(pin.getUser().getId())
                .userNickname(nickname)
                .pinProfilePic(userPic)
                .comment(comment)
                .pinDate(new DateCalc().boardListDate(pin.getCreatedAt()))
                .pinLikeCount(pinLike)
                .pinCommentCount(pinCommentSize)
                .isLiked(!ObjectUtils.isEmpty(pinLiked))
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

    public static BoardResponseDto.BoardViewPinCommentListDto toBoardPinCommentListDto(Comment comment, Long userId){
        List<String> newPinCommentPicList = new ArrayList<>();

        for (CommentPicture picture : comment.getCommentPictureList())
                newPinCommentPicList.add(picture.getPic());

        //대댓글 보는 유저가 좋아요 눌렀는지 확인
        List<CommentLike> commentLiked = comment.getCommentLikeList().stream()
                .filter(user -> user.getUser().getId().equals(userId))
                .toList();

        return BoardResponseDto.BoardViewPinCommentListDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickName())
                .comment(comment.getComment())
                .commentProfilePic(comment.getUser().getPic())
                .pinCommentDate(new DateCalc().boardListDate(comment.getCreatedAt()))
                .pinLikeCount(comment.getCommentLikeList().size())
                .isLiked(!ObjectUtils.isEmpty(commentLiked))
                .pinCommentPicList(newPinCommentPicList)
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

    public static int toPinAndCommentCount(List<Pin> pinList){
        int pinAndCommentSize = 0; // 댓글+대댓글 개수

        for (Pin pin : pinList) {
            pinAndCommentSize += 1;

            if(!ObjectUtils.isEmpty(pin.getCommentList()))
                for (Comment comment : pin.getCommentList())
                    pinAndCommentSize += 1;
        }
        return pinAndCommentSize;
    }

    public static BoardResponseDto.RootUserDetailDto titleBoardIdDto(BoardComplaint boardComplaint){
        return BoardResponseDto.RootUserDetailDto.builder()
                .boardId(boardComplaint.getBoard().getId())
                .title(boardComplaint.getBoardTitle())
                .build();
    }

    public static BoardResponseDto.RootUserReportBoardsDto rootUserReportBoardsDto(BoardComplaint boardComplaint){
        return BoardResponseDto.RootUserReportBoardsDto.builder()
                .complaintId(boardComplaint.getId())
                .boardId(boardComplaint.getBoard().getId())
                .title(boardComplaint.getBoardTitle())
                .content(boardComplaint.getBoardContent())
                .createdAt(DateCalc.formatDate2(boardComplaint.getCreatedAt()))
                .testCreatedAt(boardComplaint.getCreatedAt())
                .build();
    }
}
