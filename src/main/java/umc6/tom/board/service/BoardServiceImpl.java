package umc6.tom.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.model.Board;
import umc6.tom.board.repository.MajorRepositoryBoard;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.board.repository.PinCommentRepositoryBoard;
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
    private final PinCommentRepositoryBoard pinCommentRepositoryBoard;

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
    public Page<Board> getBoardList(Long majorId, Integer page) {
        Majors majors = majorRepositoryBoard.findById(majorId).get();

        Page<Board> boardPage = boardRepository.findAllByMajorsOrderByCreatedAtDesc(majors, PageRequest.of(page, 12));
        return boardPage;
    }
}
