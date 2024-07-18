package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Example extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String problem;

    @Column(nullable = false, length = 1000)
    private String answerProblem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;


}
