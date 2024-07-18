package umc6.tom.user.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Role;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {

    // 회원 객체 생성
    public static User toUser(UserDtoReq.JoinDto request) {

        return User.builder()
                .name(request.getName())
                .account(request.getAccount())
                .password(request.getPassword())
                .role(Role.USER)
                .build();
    }


    // 회원가입 응답
    public static UserDtoRes.JoinDto joinRes(User user) {

        return UserDtoRes.JoinDto
                .builder()
                .id(user.getId())
                .nickName(user.getUsername())
                .build();
    }

}
