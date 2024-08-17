package umc6.tom.comment.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.enums.PinBoardStatus;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@DynamicInsert
public class Pin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 50)
    public String comment;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(25) DEFAULT 'ACTIVE'")
    private PinBoardStatus status;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    public Integer report;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinPicture> pinPictureList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinLike> pinLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinComplaint> pinComplaintList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    public Board board;
}
