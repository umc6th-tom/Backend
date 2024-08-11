package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Resign extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timer;

    @Column(nullable = false, length = 50)
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)   //즐겨 찾기가 하위
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
