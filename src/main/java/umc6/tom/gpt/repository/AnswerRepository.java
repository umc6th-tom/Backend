package umc6.tom.gpt.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.gpt.dto.AnswerDto;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Answer;
import umc6.tom.notice.model.NoticePicture;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Answer findAllBy();

    List<Answer> findTop2ByOrderByCreatedAtDesc();
}
