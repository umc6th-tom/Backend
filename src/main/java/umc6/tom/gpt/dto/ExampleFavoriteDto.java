package umc6.tom.gpt.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString //필드값학인
@NoArgsConstructor //기본생성자
@AllArgsConstructor
public class ExampleFavoriteDto {

    private int id;
    private Date createdAt;
    private String exampleId;
    private int userId;


}
