package umc6.tom.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;
import umc6.tom.notice.model.Notice;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    void deleteById(Long id);

    List<Example> findAllById(Long id);
}
