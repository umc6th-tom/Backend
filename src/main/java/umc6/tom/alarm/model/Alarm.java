package umc6.tom.alarm.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import umc6.tom.alarm.model.enums.Category;
import umc6.tom.alarm.model.enums.IsRead;
import umc6.tom.board.model.Board;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, length = 50)
    private String alarm;
    
    //어떤 내용에 알림을 받았는지
    @Column(nullable = false ,length = 500)
    private String targetAlarm;

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
