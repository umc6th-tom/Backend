package umc6.tom.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.Pin;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PinBoardDto {
    private Pin pin;
    private Board board;
}
