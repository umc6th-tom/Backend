package umc6.tom.alarm.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.user.model.User;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlarmSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff pinSet;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff commentSet;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff eventSet;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff hotSet;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff likeSet;

    @Enumerated(EnumType.STRING)
    private AlarmOnOff noticeSet;

    @OneToOne(fetch = FetchType.LAZY)   //알람셋이 하위
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
