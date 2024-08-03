package umc6.tom.board.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.alarm.model.Alarm;
import umc6.tom.board.model.enums.BoardStatus;
import umc6.tom.comment.model.Pin;
import umc6.tom.common.BaseEntity;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 50)
    public String title;

    @Column(nullable = false, length = 500)
    public String content;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    public Integer report;

    public LocalDateTime popularAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    private BoardStatus status;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardPicture> boardPictureList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardComplaint> boardComplaintList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Alarm> alarmList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "majors_id")
    private Majors majors;
}
