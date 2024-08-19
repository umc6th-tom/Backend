package umc6.tom.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

public interface RootUserService {

    void managerAuth(Long userId);

    User findUser(Long userId);

    UserDtoRes.warnDto warn(Long userId, UserDtoReq.WarnDto request);

    UserDtoRes.suspensionDto suspension(Long userId, UserDtoReq.SuspensionDto request);

    Page<UserDtoRes.userFindAllDto> findAllUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findNicknameUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findNameUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findAccountUser(String keyword, Pageable adjustedPageable);

    UserDtoRes.userFindDetailDto findUserDetail(Long userId);

    Page<BoardResponseDto.RootUserReportBoardsDto> findUserReportBoards(Long boardUserId, Pageable adjustedPageable);

    Page<PinResDto.RootUserReportPinsOrCommentsPinsDto> findUserReportPins(Long pinUserId, Pageable adjustedPageable);

    UserDtoRes.complaintBoardReasonDto boardReportReason(Long userId);

    UserDtoRes.complaintCommentReasonDto pinReportReason(Long complaintId);

    UserDtoRes.complaintCommentReasonDto commentReportReason(Long complaintId);

    Page<UserDtoRes.complaintAllResDto> complaintsAll(Pageable adjustedPageable);

    Page<UserDtoRes.complaintAllResDto> complaintsBoard(Pageable adjustedPageable);

    Page<UserDtoRes.complaintAllResDto> complaintsComment(Pageable adjustedPageable);

//    UserDtoRes.suspensionDto reinstatement(Long userId);
}
