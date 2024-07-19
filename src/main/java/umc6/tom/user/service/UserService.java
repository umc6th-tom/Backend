package umc6.tom.user.service;

import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

public interface UserService {

    User join(UserDtoReq.JoinDto request);

    User loadUserByUsername(String username);

    UserDtoRes.signInDto signIn(UserDtoReq.SignInDto request);

    boolean duplicatedNickName(String nickName);

    boolean duplicatedAccount(String account);

    boolean duplicatedPhone(String phone);
}
