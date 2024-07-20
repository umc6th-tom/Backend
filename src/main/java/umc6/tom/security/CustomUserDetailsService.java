package umc6.tom.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(userId))
                .map(this::createUserDetails)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체를 만들어 return
    private UserDetails createUserDetails(umc6.tom.user.model.User user) {
        return User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(String.valueOf(user.getRole()))
                .build();
    }
}
