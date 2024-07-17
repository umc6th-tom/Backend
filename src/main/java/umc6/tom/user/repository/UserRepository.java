package umc6.tom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

}
