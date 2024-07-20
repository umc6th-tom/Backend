package umc6.tom.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.gpt.dto.ExampleFavoriteDto;

public interface ExampleFavoriteRepository extends JpaRepository<ExampleFavoriteDto, Long> {
}
