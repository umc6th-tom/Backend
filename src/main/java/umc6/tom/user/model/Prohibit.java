package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.board.model.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Prohibit {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String message;

    @Column
    private String division;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column
    private LocalDateTime suspensionDue;
}