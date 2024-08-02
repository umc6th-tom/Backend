package umc6.tom.alarm.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import umc6.tom.alarm.model.enums.Field;
import umc6.tom.alarm.model.enums.IsRead;
import umc6.tom.board.model.Board;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Field field;

    @Column(nullable = false, length = 50)
    private String alarm;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(3) DEFAULT 'NO'")
    private IsRead isRead;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
