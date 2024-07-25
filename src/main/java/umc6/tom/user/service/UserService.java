package umc6.tom.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

public interface UserService {

    User join(UserDtoReq.JoinDto request);

    boolean checkNickName(UserDtoReq.CheckNickNameDto request);

    boolean checkAccount(UserDtoReq.CheckAccountDto request);

    UserDtoRes.PhoneAuthDto phoneAuth(UserDtoReq.PhoneDto request);

    UserDtoRes.LoginDto login(HttpServletRequest request, HttpServletResponse response, UserDtoReq.LoginDto req);

    UserDtoRes.ReissueDto reissue(String refreshToken);

    boolean duplicatedNickName(String nickName);

    boolean duplicatedAccount(String account);

    void withDraw(Long userId, UserDtoReq.WithDrawDto request);

    void deleteUser();

    void logout(HttpServletRequest request, HttpServletResponse response, String accessToken);

    LocalDateTime convertToLocalDateTime(long timestampMillis);

    UserDtoRes.FindAccountDto findAccount(UserDtoReq.FindAccountDto request);

    UserDtoRes.FindPasswordDto findPassword(UserDtoReq.FindPasswordDto request);

    void findRestorePassword(UserDtoReq.FindRestorePasswordDto request);

    void restoreAccount(Long userId, UserDtoReq.RestoreAccountDto request);

    void restorePassword(Long userId, UserDtoReq.RestorePasswordDto request);

    void restoreNickName(Long userId, UserDtoReq.RestoreNickNameDto request);

    void restorePhone(Long userId, UserDtoReq.PhoneDto request);

    void restoreMajor(Long userId, UserDtoReq.RestoreMajorDto request);

    UserDtoRes.ChangeAgreementDto changeAgreement(Long userId);
}
