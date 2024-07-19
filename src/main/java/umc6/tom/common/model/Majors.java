package umc6.tom.common.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.board.model.Board;
import umc6.tom.board.model.BoardLike;
import umc6.tom.common.BaseEntity;
import umc6.tom.common.model.enums.Major;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Majors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Major major;

    @OneToMany(mappedBy = "majors", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

//    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL)
//    private List<User> userList = new ArrayList<>();

}
