package umc6.tom.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.pin.model.Pin;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
}
