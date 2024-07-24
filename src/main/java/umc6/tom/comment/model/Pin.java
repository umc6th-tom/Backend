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
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Pin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 50)
    public String comment;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinPicture> pinPictureList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinLike> pinLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    public List<PinComment> pinCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    public Board board;
}
