package umc6.tom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinComplaint;

import java.util.List;

@Repository
public interface PinComplaintRepository extends JpaRepository<PinComplaint, Long> {
    List<PinComplaint> findAllByPinUserId(Long id);
}
