package umc6.tom.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.board.model.BoardComplaint;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComplaint;

import java.util.List;

@Repository
public interface PinComplaintRepository extends JpaRepository<PinComplaint, Long> {
    List<PinComplaint> findAllByPinUserId(Long id);

    List<PinComplaint> findAllByPinUserIdOrderByCreatedAtDesc(Long writeUserId);

    List<PinComplaint> findAllByPinIdOrderByCreatedAtDesc(Long id);

    List<PinComplaint> findAllByOrderByCreatedAtDesc();

    List<PinComplaint> findAllByPin(Pin pin);
}
