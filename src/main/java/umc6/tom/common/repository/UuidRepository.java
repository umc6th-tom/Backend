package umc6.tom.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.common.model.Uuid;

@Repository
public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
