package umc6.tom.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.board.repository.BoardLikeRepository;
import umc6.tom.board.repository.MajorRepositoryBoard;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.enums.Status;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MajorRepositoryBoard majorRepositoryBoard;
    private final BoardLikeRepository boardLikeRepository;

    @Override
    public Board registerBoard(BoardRequestDto.RegisterDto request, Long userId) {
        Board newBoard = BoardConverter.toBoard(request);
        User user = userRepository.findById(userId).get();
        Majors majors = majorRepositoryBoard.findById(request.getMajor()).get();

        newBoard.setUser(user);
        newBoard.setMajors(majors);
        newBoard.setStatus(Status.ACTIVE);

        return boardRepository.save(newBoard);
    }

    @Override
    public Page<Board> getBoardMajorList(Long majorId, Integer page) {
        Majors majors = majorRepositoryBoard.findById(majorId).get();

        Page<Board> boardPage = boardRepository.findAllByMajorsOrderByCreatedAtDesc(majors, PageRequest.of(page, 12));
        return boardPage;
    }

    @Override
    public Page<Board> getBoardAllList(Integer page) {
        Page<Board> boardPage= boardRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 12));

        return boardPage;
    }

    @Override
    public BoardLike addBoardLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).get();
        Board board = boardRepository.findById(boardId).get();
        BoardLike boardLike = BoardConverter.toBoardLike(user, board);
        //좋아요 연타로 인한 api 여러번 실행 방지
        if (boardLikeRepository.existsBoardLikeByUserAndBoard(user, board))
            throw new BoardHandler(ErrorStatus.BoardLike_DUPLICATED);
        return boardLikeRepository.save(boardLike);
    }

    @Transactional
    @Override
    public BoardLike deleteBoardLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).get();
        Board board = boardRepository.findById(boardId).get();
        BoardLike boardLike = BoardConverter.toBoardLike(user, board);
        //좋아요 연타로 인한 api 여러번 실행 방지
        if (!boardLikeRepository.existsBoardLikeByUserAndBoard(user, board))
            throw new BoardHandler(ErrorStatus.BoardLike_NOT_FOUND);
        boardLikeRepository.deleteBoardLikeByUserAndBoard(user, board);
        return boardLike;
    }
}
