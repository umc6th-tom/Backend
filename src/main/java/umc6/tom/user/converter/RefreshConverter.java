package umc6.tom.user.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import umc6.tom.user.dto.ResignDtoReq;
import umc6.tom.user.model.RefreshToken;
import umc6.tom.user.model.Resign;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshConverter {

    public static RefreshToken toRefreshToken(Long userId, String refreshToken) {

        return RefreshToken.builder()
                .userId(userId)
                .refreshTokenValue(refreshToken)
                .build();
    }
}
