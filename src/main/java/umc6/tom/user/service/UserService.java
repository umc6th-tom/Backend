package umc6.tom.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    User join(UserDtoReq.JoinDto request);

    UserDtoRes.PhoneAuthDto phoneAuth(UserDtoReq.PhoneDto request);

    UserDtoRes.LoginDto login(HttpServletRequest request, HttpServletResponse response, UserDtoReq.LoginDto req);

    UserDtoRes.ReissueDto reissue(String refreshToken);

    boolean duplicatedNickName(String nickName);

    boolean duplicatedAccount(String account);

    void withDraw(Long userId, UserDtoReq.WithDrawDto request);

    void deleteUser();

    void logout(HttpServletRequest request, HttpServletResponse response, String accessToken);

    LocalDateTime convertToLocalDateTime(long timestampMillis);

    UserDtoRes.FindAccountDto findAccount(String phone);

    UserDtoRes.FindPasswordDto findPassword(String phone);

    void findRestorePassword(UserDtoReq.FindRestorePasswordDto request);

    void restoreAccount(Long userId, UserDtoReq.RestoreAccountDto request);

    void restorePassword(Long userId, UserDtoReq.RestorePasswordDto request);

    void restoreNickName(Long userId, UserDtoReq.RestoreNickNameDto request);

    void restorePhone(Long userId, UserDtoReq.PhoneDto request);

    void restoreMajor(Long userId, UserDtoReq.RestoreMajorDto request);

    UserDtoRes.ChangeAgreementDto changeAgreement(Long userId);

    UserDtoRes.RestorePic restorePic(Long userId, MultipartFile request);

    void restorePicDef(Long userId);

    UserDtoRes.FindProfileDto findProfile(Long userId);

    Page<BoardResponseDto.HistoryDto> findHistoryAll(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.HistoryDto> findMyBoards(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.HistoryDto> findMyComments(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.HistoryDto> findMyLikes(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.FindUserBoardsDto> findProfileBoards(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.FindUserBoardsDto> findProfileComments(Long userId, Pageable adjustedPageable);

    Page<BoardResponseDto.HistoryDto> findTextHistoryAll(Long userId, Pageable adjustedPageable,String content );

    Page<BoardResponseDto.HistoryDto> findTextHistoryBoards(Long userId, Pageable adjustedPageable, String content);

    Page<BoardResponseDto.HistoryDto> findTextHistoryComments(Long userId, Pageable adjustedPageable, String content);

    Page<BoardResponseDto.HistoryDto> findTextHistoryLikes(Long userId, Pageable adjustedPageable, String content);

    Page<UserDtoRes.userFindAllDto> findAllUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findNicknameUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findNameUser(String keyword, Pageable adjustedPageable);

    Page<UserDtoRes.userFindAllDto> findAccountUser(String keyword, Pageable adjustedPageable);

    UserDtoRes.userFindDetailDto findUserDetail(Long userId);

    UserDtoRes.warnDto warn(Long userId, Long targetUserId, UserDtoReq.WarnDto request);

    UserDtoRes.suspendDto suspension(Long userId, Long targetUserId, UserDtoReq.SuspendDto request);

    void managerAuth(Long userId);

    User findUser(Long userId);

    Page<BoardResponseDto.RootUserReportBoardsDto> findUserReportBoards(Long boardUserId,Pageable adjustedPageable);
}
