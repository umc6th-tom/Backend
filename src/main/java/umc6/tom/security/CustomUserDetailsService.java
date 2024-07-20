package umc6.tom.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk)  {

        User user = userRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        return new CustomUserDetail(user);	// 위에서 생성한 CustomUserDetails Class
    }
}
