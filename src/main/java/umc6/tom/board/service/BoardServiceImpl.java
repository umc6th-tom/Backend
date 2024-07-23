package umc6.tom.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.board.model.BoardLike;
import umc6.tom.board.repository.*;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComment;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.enums.Status;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

import javax.xml.stream.events.Comment;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MajorRepositoryBoard majorRepositoryBoard;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardComplaintRepository boardComplaintRepository;
    private final PinRepositoryBoard pinRepositoryBoard;
    private final PinCommentRepositoryBoard pinCommentRepositoryBoard;

    @Override
    public Board registerBoard(BoardRequestDto.RegisterDto request, Long userId) {
        Board newBoard = BoardConverter.toBoard(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Majors majors = majorRepositoryBoard.findById(request.getMajor()).orElseThrow(() -> new BoardHandler(ErrorStatus.MAJORS_NOR_FOUND));

        newBoard.setUser(user);
        newBoard.setMajors(majors);
        newBoard.setStatus(Status.ACTIVE);

        return boardRepository.save(newBoard);
    }

    @Override
    public Page<Board> getBoardMajorList(Long majorId, Integer page) {
        Majors majors = majorRepositoryBoard.findById(majorId).orElseThrow(() -> new BoardHandler(ErrorStatus.MAJORS_NOR_FOUND));

        Page<Board> boardPage = boardRepository.findAllByStatusAndMajorsOrderByCreatedAtDesc(Status.ACTIVE, majors, PageRequest.of(page, 12));
        return boardPage;
    }

    @Override
    public Page<Board> getBoardAllList(Integer page) {
        Page<Board> boardPage= boardRepository.findAllByStatusOrderByCreatedAtDesc(Status.ACTIVE, PageRequest.of(page, 12));

        return boardPage;
    }

    @Override
    public BoardLike addBoardLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        BoardLike boardLike = BoardConverter.toBoardLike(user, board);
        //좋아요 연타로 인한 api 여러번 실행 방지
        if (boardLikeRepository.existsBoardLikeByUserAndBoard(user, board))
            throw new BoardHandler(ErrorStatus.BOARDLIKE_DUPLICATED);
        return boardLikeRepository.save(boardLike);
    }

    @Transactional
    @Override
    public BoardLike deleteBoardLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        BoardLike boardLike = BoardConverter.toBoardLike(user, board);

        //좋아요 연타로 인한 api 여러번 실행 방지
        if (!boardLikeRepository.existsBoardLikeByUserAndBoard(user, board))
            throw new BoardHandler(ErrorStatus.BOARDLIKE_NOT_FOUND);

        boardLikeRepository.deleteBoardLikeByUserAndBoard(user, board);
        return boardLike;
    }

    @Override
    @Transactional
    public Board deleteBoard(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        //타인 게시글 삭제 못하게
        if (!board.getUser().getId().equals(userId))
            throw new BoardHandler(ErrorStatus.BOARD_USER_NOT_MATCH);

        //댓글 작성 됐거나, 핫한 게시글시 삭제 수정 못함.
        //댓글 없고 대댓글만 있을 때 조건 추가야함*
        if (!board.getPinList().isEmpty() || board.getPopularAt()!=null )
            throw new BoardHandler(ErrorStatus.BOARD_CANNOT_DELETE);

        boardRepository.delete(board);
        return board;
    }

    @Override
    public BoardComplaint complaintBoard(BoardRequestDto.AddComplaintDto request, Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        BoardComplaint boardComplaint = BoardConverter.toBoardComplaint(request, user, board);
        return boardComplaintRepository.save(boardComplaint);
    }

    @Override
    public Page<Board> getSearchAllBoardList(String searchType, String searchKeyword, Integer page) {
        Page<Board> boardPage;

        if (searchType.equals("title"))
            boardPage = boardRepository.findAllByTitleContainingOrderByCreatedAtDesc
                    (searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("content"))
            boardPage = boardRepository.findAllByContentContainingOrderByCreatedAtDesc
                    (searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("title-content"))
            boardPage = boardRepository.findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc
                    (searchKeyword, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("nickname"))
            boardPage = boardRepository.findAllByUser_NickNameContainingOrderByCreatedAtDesc
                    (searchKeyword, PageRequest.of(page, 12));

        else
            throw new BoardHandler(ErrorStatus.BOARD_SEARCHTYPE_NOT_FOUND);
        
        //검색한 결과가 없을 때
        if (boardPage.isEmpty())
            throw new BoardHandler(ErrorStatus.BOARD_NOT_SEARCH);

        return boardPage;
    }

    @Override
    public Page<Board> getMyBoardList(Long userId, Integer page) {
        Page<Board> boardPage = boardRepository.findAllByUserIdOrderByCreatedAtDesc
                (userId, PageRequest.of(page, 12));

        return boardPage;
    }
}
