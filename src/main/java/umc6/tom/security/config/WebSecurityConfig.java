package umc6.tom.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import umc6.tom.user.service.UserService;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserService userService;

}
