package umc6.tom.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.alarm.repository.AlarmSetRepository;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.AlarmSetHandler;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.dto.BoardRequestDto;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.board.model.*;
import umc6.tom.board.model.enums.BoardStatus;
import umc6.tom.board.repository.*;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.repository.PinRepository;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.Uuid;
import umc6.tom.common.repository.UuidRepository;
import umc6.tom.config.AmazonConfig;
import umc6.tom.firebase.service.PushMessage;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;
import umc6.tom.util.AmazonS3Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MajorRepositoryBoard majorRepositoryBoard;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardComplaintRepository boardComplaintRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final BoardComplaintPictureRepository boardComplaintPictureRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Util amazonS3Util;
    private final AmazonConfig amazonConfig;
    private final AlarmSetRepository alarmSetRepository;
    private final PushMessage pushMessage;
    private final PinRepository pinRepository;

    @Override
    public Board registerBoard(BoardRequestDto.RegisterDto request, Long userId, MultipartFile[] files) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Majors majors = majorRepositoryBoard.findById(request.getMajorId()).orElseThrow(()
                -> new BoardHandler(ErrorStatus.MAJORS_NOR_FOUND));
        Board newBoard = BoardConverter.toBoard(request, user, majors);

        String fileName = null;
        if (!ObjectUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                    fileName = amazonS3Util.upload(file, amazonConfig.getBoardPath(), savedUuid);
                } catch (IOException e) {
                    throw new BoardHandler(ErrorStatus.BOARD_FILE_UPLOAD_FAILED);
                }
                BoardPicture newboardPicture = BoardConverter.toBoardPicture(newBoard, fileName);
                boardPictureRepository.save(newboardPicture);
            }
        }
        return boardRepository.save(newBoard);
    }

    @Override
    public Page<Board> getBoardMajorList(Long majorId, Integer page) {
        Majors majors = majorRepositoryBoard.findById(majorId).orElseThrow(()
                -> new BoardHandler(ErrorStatus.MAJORS_NOR_FOUND));

        Page<Board> boardPage = boardRepository.findAllByStatusAndMajorsOrderByCreatedAtDesc
                (BoardStatus.ACTIVE, majors, PageRequest.of(page, 12));
        return boardPage;
    }

    @Override
    public Page<Board> getBoardAllList(Integer page) {
        Page<Board> boardPage = boardRepository.findAllByStatusOrderByCreatedAtDesc
                (BoardStatus.ACTIVE, PageRequest.of(page, 12));

        return boardPage;
    }

    @Override
    public Page<Board> getBoardHotList(Integer page, Long majorId) {
        Page<Board> boardPage = boardRepository.findAllByStatusAndMajorsIdAndPopularAtIsNotNullOrderByCreatedAtDesc
                (BoardStatus.ACTIVE, majorId, PageRequest.of(page, 12));

        return boardPage;
    }

    @Override
    public BoardResponseDto.BoardViewDto getBoardView(Long boardId, Integer page) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        //신고로 안보이게 된 게시글 조회 방지
        if (!board.getStatus().equals(BoardStatus.ACTIVE))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_FOUND);
        Page<Pin> pinPage= pinRepository.findAllByBoardIdOrderByCreatedAtAsc(boardId, PageRequest.of(page, 10));
        return BoardConverter.toBoardViewDto(board, pinPage);
    }

    @Override
    public BoardResponseDto.BoardMainViewListDto getBoardMainList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        List<Board> boardMajorList = boardRepository.findTop5ByStatusAndMajorsOrderByCreatedAtDesc
                (BoardStatus.ACTIVE, user.getMajors());
        List<Board> boardhotList = boardRepository.findTop5ByStatusAndPopularAtNotNullOrderByCreatedAtDesc(BoardStatus.ACTIVE);
        List<Board> boardAllList = boardRepository.findTop5ByStatusOrderByCreatedAtDesc(BoardStatus.ACTIVE);
        BoardResponseDto.BoardMainViewListDto boardMainList = BoardConverter.toBoardMainListViewListDto
                (boardMajorList, boardhotList, boardAllList);
        return boardMainList;
    }

    @Override
    public BoardLike addBoardLike(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        //자신의 글에는 좋아요 못함
        if (board.getUser().getId().equals(userId))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_LIKE);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //좋아요 연타로 인한 api 여러번 실행 방지
        if (boardLikeRepository.existsBoardLikeByUserAndBoard(user, board))
            throw new BoardHandler(ErrorStatus.BOARDLIKE_DUPLICATED);
        //핫한 게시글 조건이 만족 -> 불만족 -> 만족 되었을 때 핫한 게시글 시간 갱신되는 경우 대비
        if (board.getPopularAt() == null && board.getBoardLikeList().size() >= 10) {
            board.setPopularAt(LocalDateTime.now());
            boardRepository.save(board);
        }
        BoardLike boardLike = BoardConverter.toBoardLike(user, board);
        // 좋아요 알림 유무
        AlarmSet alarmSet = alarmSetRepository.findByUserId(board.getUser().getId()).orElseThrow(()
                -> new AlarmSetHandler(ErrorStatus.ALARM_SET_NOT_FOUND));
        //좋아요 알림 보내기
        if (alarmSet.getLikeSet().equals(AlarmOnOff.ON))
            pushMessage.boardLikeNotification(board, user);

        return boardLikeRepository.save(boardLike);
    }

    @Override
    @Transactional
    public BoardLike deleteBoardLike(Long userId, Long boardId) {
        log.info(amazonConfig.getRegion());
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
        List<String> boardPicList = BoardConverter.toPicStringList(board.getBoardPictureList());
        //타인 게시글 삭제 못하게
        if (!board.getUser().getId().equals(userId))
            throw new BoardHandler(ErrorStatus.BOARD_USER_NOT_MATCH);

        //댓글 작성 됐거나, 핫한 게시글, 신고 상태에선 삭제 못함.
        //댓글 없고 대댓글만 있을 때 조건 추가야함*
        if (!ObjectUtils.isEmpty(board.getPinList()) || board.getPopularAt() != null)
            throw new BoardHandler(ErrorStatus.BOARD_CANNOT_DELETE);
        //신고 기록없을 때만 실제 데이터 삭제
        //신고 상태에서 삭제하면 화면에만 안보이고 실제 데이터 보존
        if (board.getStatus().equals(BoardStatus.ACTIVE) && board.getReport() == 0) {
            for (String deletedPic : boardPicList)
                amazonS3Util.deleteFile(deletedPic);
            boardRepository.delete(board);
        } else if (board.getReport() >= 1) {
            board.setStatus(BoardStatus.COMPLAINTDELETE);
            boardRepository.save(board);
        }

        return board;
    }

    @Override
    @Transactional
    public Board updateBoard(BoardRequestDto.UpdateBoardDto request, Long userId, Long boardId, MultipartFile[] files) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));

        //타인 게시글 수정 못하게
        if (!board.getUser().getId().equals(userId))
            throw new BoardHandler(ErrorStatus.BOARD_USER_NOT_MATCH);

        //댓글 작성 됐거나, 핫한 게시글시 수정 못함. 게시글 삭제, 신고 누적으로 게시글 안보이게 되어도 못함
        if (!ObjectUtils.isEmpty(board.getPinList()) || board.getPopularAt() != null ||
                !board.getStatus().equals(BoardStatus.ACTIVE))
            throw new BoardHandler(ErrorStatus.BOARD_CANNOT_UPDATE);

        board.setContent(request.getContent());
        board.setTitle(request.getTitle());
        Board savedBoard = boardRepository.save(board);

        String fileName = null;
        if (!ObjectUtils.isEmpty(files)) {
            BoardPicture newboardPicture = null;
            for (MultipartFile file : files) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

                    fileName = amazonS3Util.upload(file, amazonConfig.getBoardPath(), savedUuid);
                } catch (IOException e) {
                    throw new BoardHandler(ErrorStatus.BOARD_FILE_UPLOAD_FAILED);
                }
                newboardPicture = BoardConverter.toBoardPicture(board, fileName);
            }
            if (!ObjectUtils.isEmpty(board.getBoardPictureList())) {
                //수정으로 삭제된 사진만 남음(중복안된 값)
                List<String> PicUrl = BoardConverter.toPicStringIdList(board.getBoardPictureList()).stream().
                        filter(o -> request.getPic().stream().noneMatch(Predicate.isEqual(o)))
                        .toList();
                for (String pic : PicUrl) {
                    //신고된적 없을시 실제 버킷 사진 데이터 삭제
                    if (board.getStatus().equals(BoardStatus.ACTIVE))
                        amazonS3Util.deleteFileNoPreUrl(amazonConfig.getBoardPath() + "/" + pic);

                    String deletePic = fileName.substring(0, 46) + pic;
                    boardPictureRepository.deleteByPicUrl(deletePic);
                }
            }
            boardPictureRepository.save(newboardPicture);
        }

        return savedBoard;
    }

    @Override
    public BoardComplaint complaintBoard(BoardRequestDto.AddComplaintDto request, Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARD_NOT_FOUND));
        //자기자신 신고 못함
        if (board.getUser().getId().equals(userId))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_COMPLAINT);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        board.setReport(board.getReport() + 1);
        if (board.getReport() == 10)
            board.setStatus(BoardStatus.OVERCOMPLAINT);

        boardRepository.save(board);

        BoardComplaint boardComplaint = BoardConverter.toBoardComplaint(request, user, board);
        boardComplaintRepository.save(boardComplaint);
        BoardComplaintPicture boardComplaintPicture;
        for (BoardPicture pic : board.getBoardPictureList()) {
            boardComplaintPicture = BoardConverter.toBoardComplaintPictureDto(boardComplaint, pic.getPic());
            boardComplaintPictureRepository.save(boardComplaintPicture);
        }
        // 신고당한 유저 신고 누적 +1
        User boardUser = userRepository.findById(board.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        boardUser.setReport(board.getUser().getReport() + 1);
        userRepository.save(boardUser);
        return boardComplaint;
    }

    @Override
    public Page<Board> getSearchAllBoardList(String searchType, String searchKeyword, Integer page) {
        Page<Board> boardPage;

        if (searchType.equals("title"))
            boardPage = boardRepository.findAllByStatusAndTitleContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("content"))
            boardPage = boardRepository.findAllByStatusAndContentContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("title-content"))
            boardPage = boardRepository.findAllByStatusAndTitleContainingOrContentContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("nickname"))
            boardPage = boardRepository.findAllByStatusAndUser_NickNameContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));
        else
            throw new BoardHandler(ErrorStatus.BOARD_SEARCHTYPE_NOT_FOUND);

        //검색한 결과가 없을 때
        if (ObjectUtils.isEmpty(boardPage))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_SEARCH);

        return boardPage;
    }

    @Override
    public Page<Board> getSearchMajorBoardList(Long majorId, String searchType, String searchKeyword, Integer page) {
        Page<Board> boardPage;

        if (searchType.equals("title"))
            boardPage = boardRepository.findAllByStatusAndMajorsIdAndTitleContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, majorId, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("content"))
            boardPage = boardRepository.findAllByStatusAndMajorsIdAndContentContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, majorId, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("title-content"))
            boardPage = boardRepository.findAllByStatusAndMajorsIdAndTitleContainingOrContentContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, majorId, searchKeyword, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("nickname"))
            boardPage = boardRepository.findAllByStatusAndMajorsIdAndUser_NickNameContainingOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, majorId, searchKeyword, PageRequest.of(page, 12));
        else
            throw new BoardHandler(ErrorStatus.BOARD_SEARCHTYPE_NOT_FOUND);

        //검색한 결과가 없을 때
        if (ObjectUtils.isEmpty(boardPage))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_SEARCH);

        return boardPage;
    }

    @Override
    public Page<Board> getSearchHotBoardList(String searchType, String searchKeyword, Integer page) {
        Page<Board> boardPage;

        if (searchType.equals("title"))
            boardPage = boardRepository.findAllByStatusAndTitleContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("content"))
            boardPage = boardRepository.findAllByStatusAndContentContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("title-content"))
            boardPage = boardRepository.findAllByStatusAndTitleContainingOrContentContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, searchKeyword, PageRequest.of(page, 12));

        else if (searchType.equals("nickname"))
            boardPage = boardRepository.findAllByStatusAndUser_NickNameContainingAndPopularAtIsNotNullOrderByCreatedAtDesc
                    (BoardStatus.ACTIVE, searchKeyword, PageRequest.of(page, 12));
        else
            throw new BoardHandler(ErrorStatus.BOARD_SEARCHTYPE_NOT_FOUND);

        //검색한 결과가 없을 때
        if (ObjectUtils.isEmpty(boardPage))
            throw new BoardHandler(ErrorStatus.BOARD_NOT_SEARCH);

        return boardPage;
    }
}
