package umc6.tom.user.converter;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.common.model.Majors;
import umc6.tom.user.dto.UserDtoReq;
import umc6.tom.user.dto.UserDtoRes;
import umc6.tom.user.model.User;

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
                .build();
    }


    // 회원가입 응답
    public static UserDtoRes.JoinDto joinRes(User user) {

        return UserDtoRes.JoinDto.builder()
                .id(user.getId())
                .nickName(user.getUsername())
                .build();
    }

    // 로그인 응답
    public static UserDtoRes.LoginDto signInRes(User user, String accessToken, String refreshToken) {

        return UserDtoRes.LoginDto.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

    // accessToken 재발급 응답
    public static UserDtoRes.ReissueDto reissueRes(String accessToken) {

        return UserDtoRes.ReissueDto.builder()
                .accessToken(accessToken)
                .build();
    }

    // 아이디 찾기 응답
    public static UserDtoRes.FindAccountDto findAccountRes(User user) {

        return UserDtoRes.FindAccountDto.builder()
                .account(user.getAccount())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 비밀번호 찾기 응답
    public static UserDtoRes.FindPasswordDto findPasswordRes(User user) {

        return UserDtoRes.FindPasswordDto.builder()
                .userId(user.getId())
                .build();
    }

}
