package umc6.tom.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc6.tom.board.model.Board;
import umc6.tom.comment.model.Comment;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentBoardDto {
    private Comment comment;
    private Board board;
}
