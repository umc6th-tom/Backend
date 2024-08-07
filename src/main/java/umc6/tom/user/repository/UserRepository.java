package umc6.tom.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    Optional<User> findByAccountAndStatusIn(String account, List<UserStatus> statuses);

    Optional<User> findByAccountAndStatus(String account, UserStatus status);

    Optional<User> findByNickName(String nickName);

    Optional<User> findByAccount(String account);

    Optional<User> findByPhone(String phone);

    List<User> findByStatusAndUpdatedAtBefore(UserStatus status, LocalDateTime updatedAt);

    Page<User> findAllByNameContainingOrAccountContainingOrNickNameContainingOrderByCreatedAtDesc(String name, String account, String nickName, Pageable pageable);

    Page<User> findAllByNickNameContainingOrderByCreatedAtDesc(String keyword, Pageable adjustedPageable);

    Page<User> findAllByNameContainingOrderByCreatedAtDesc(String keyword, Pageable adjustedPageable);

    Page<User> findAllByAccountContainingOrderByCreatedAtDesc(String keyword, Pageable adjustedPageable);
}
