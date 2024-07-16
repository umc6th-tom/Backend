package umc6.tom.user.service;

import umc6.tom.user.model.User;

public interface UserDetailService {

    User loadUserByUsername(String username);
}
