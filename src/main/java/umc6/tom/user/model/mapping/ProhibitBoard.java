package umc6.tom.user.model.mapping;

import jakarta.persistence.*;
import umc6.tom.board.model.Board;
import umc6.tom.user.model.Prohibit;

@Entity
public class ProhibitBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "prohibit_id")
    private Prohibit prohibit;
}
