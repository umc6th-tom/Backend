package umc6.tom.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc6.tom.gpt.dto.ExampleFavoriteDto;
import umc6.tom.gpt.model.ExampleFavorite;
import umc6.tom.user.model.User;

import java.util.List;

@Repository
public interface ExampleFavoriteRepository extends JpaRepository<ExampleFavorite, Long> {
//    @Query("SELECT ef.example FROM ExampleFavorite ef WHERE ef.user = :userId order by ef.createdAt ASC")
//    List<Long> findExampIdsByUserOrderByCreatedAtAsc(@Param("user") Long userId);

    List<ExampleFavorite> findExampleFavoritesByUserOrderByCreatedAtAsc(User user);
}
