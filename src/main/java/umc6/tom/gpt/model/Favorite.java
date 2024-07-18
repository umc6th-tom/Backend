package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "favorite", fetch = FetchType.LAZY) //즐겨 찾기가 하위
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
