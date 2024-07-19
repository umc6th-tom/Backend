package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String question;

    @Column(nullable = false, length = 50)
    private String questionTag;

    @Column(nullable = false, length = 1000)
    private String content;

    @OneToOne(fetch = FetchType.LAZY) //답변이 주체(주인)이고 즐겨찾기가 하위개념
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Example> exampleList = new ArrayList<>();

}
