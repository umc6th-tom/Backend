package umc6.tom.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException((username + " not found")));    // 예외 처리 통합으로 진행!
    }
}
