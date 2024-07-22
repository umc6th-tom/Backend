package umc6.tom.gpt.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private int id;
    private String question;
    private String questionTag;
    private String content;
    private Date timer;
    private int userId;


}
