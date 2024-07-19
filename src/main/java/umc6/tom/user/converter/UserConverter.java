package umc6.tom.user.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.common.model.Majors;
import umc6.tom.common.model.enums.Major;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Role;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {

    // 회원 객체 생성
    public static User toUser(UserDtoReq.JoinDto request, Majors major) {

        return User.builder()
                .name(request.getName())
                .nickName(request.getNickName())
                .account(request.getAccount())
                .password(request.getPassword())
                .phone(request.getPhone())
                .majors(major)
                .role(Role.USER)
                .build();
    }


    // 회원가입 응답
    public static UserDtoRes.JoinDto joinRes(User user) {

        return UserDtoRes.JoinDto.builder()
                .id(user.getId())
                .nickName(user.getUsername())
                .build();
    }

    public static UserDtoRes.signInDto signInRes(User user, String accessToken, String refreshToken) {

        return UserDtoRes.signInDto.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
