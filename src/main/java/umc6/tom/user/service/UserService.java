package umc6.tom.user.service;

import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    User join(UserDtoReq.JoinDto request);

    UserDtoRes.signInDto signIn(UserDtoReq.SignInDto request);

    boolean duplicatedNickName(String nickName);

    boolean duplicatedAccount(String account);

    void withDraw(Long userId, UserDtoReq.WithDrawDto request);

    void deleteUser();

    LocalDateTime convertToLocalDateTime(long timestampMillis);
}
