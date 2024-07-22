package umc6.tom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshTokenValue(String refreshToken);

    Optional<RefreshToken> findByUserId(Long userId);
}
