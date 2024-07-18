package umc6.tom.user.service;

import umc6.tom.security.JwtToken;
import umc6.tom.user.model.User;

public interface UserService {

    User loadUserByUsername(String username);

    JwtToken signIn(String name, String password);
}
