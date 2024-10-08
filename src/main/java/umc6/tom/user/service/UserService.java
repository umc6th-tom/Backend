package umc6.tom.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.board.dto.BoardResponseDto;
import umc6.tom.kakao.dto.KakaoUserInfoResponseDto;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

public interface UserService {

    User join(UserDtoReq.JoinDto request);

    User joinKakao(UserDtoRes.kakaoJoinDto kakaoRequest, UserDtoReq.JoinDto request);

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

    User findUser(Long userId);

    boolean existUser(Long userId);

    UserDtoRes.getMajor getMajor(Long userId);
}
