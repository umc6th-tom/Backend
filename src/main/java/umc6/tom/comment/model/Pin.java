package umc6.tom.comment.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.board.model.Board;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String comment;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<PinPicture> pinPictureList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<PinLike> pinLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<PinComment> pinCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
