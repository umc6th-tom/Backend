package umc6.tom.gpt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import umc6.tom.common.BaseEntity;
import umc6.tom.common.model.College;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String question;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "timer")
    private Date timer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "majors_id")
    private Majors majors;

    @OneToOne(mappedBy = "answerId", cascade = CascadeType.ALL)
    private Example example;

    @PrePersist
    protected void onCreate() {
        if (this.timer == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_YEAR, 3); // 3주 후로 설정
            this.timer = calendar.getTime();
        }
    }
}
