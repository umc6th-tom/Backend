package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String problem;

    @Column(nullable = false, length = 1000)
    private String answer;

    @Column(nullable = false, length = 40)
    private String tag;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answerId;

    @OneToMany(mappedBy = "example" ,cascade = CascadeType.ALL)
    private List<ExampleFavorite> exampleFavoriteList = new ArrayList<>();
}
