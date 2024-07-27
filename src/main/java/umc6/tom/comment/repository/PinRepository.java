package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.Pin;

import java.util.List;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findTop30ByUserIdOrderByCreatedAtDesc(Long id);
}
