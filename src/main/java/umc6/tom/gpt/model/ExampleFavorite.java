package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExampleFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "example_id")
    private Example example;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Static factory method
    public static ExampleFavorite createExampleFavorite(Example example, User user) {
        ExampleFavorite exampleFavorite = new ExampleFavorite();
        exampleFavorite.example = example;
        exampleFavorite.user = user;
        exampleFavorite.createdAt = LocalDateTime.now();
        return exampleFavorite;
    }
}
