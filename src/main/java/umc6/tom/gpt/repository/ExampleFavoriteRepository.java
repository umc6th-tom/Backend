package umc6.tom.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc6.tom.gpt.dto.ExampleFavoriteDto;
import umc6.tom.gpt.model.ExampleFavorite;

public interface ExampleFavoriteRepository extends JpaRepository<ExampleFavorite, Long> {
}
