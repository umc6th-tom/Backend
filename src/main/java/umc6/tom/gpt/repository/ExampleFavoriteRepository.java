package umc6.tom.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc6.tom.gpt.dto.ExampleFavoriteDto;
import umc6.tom.gpt.model.ExampleFavorite;

import java.util.List;

public interface ExampleFavoriteRepository extends JpaRepository<ExampleFavorite, Long> {
    @Query("SELECT ef.example FROM ExampleFavorite ef WHERE ef.user = :userId order by ef.createdAt ASC")
    List<Long> findExampIdsByUserIdOrderByCreatedAtAsc(@Param("userId") Long userId);
}
