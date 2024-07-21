package umc6.tom.gpt.dto;

import lombok.*;

@Getter
@Setter
@ToString //필드값학인
@NoArgsConstructor //기본생성자
@AllArgsConstructor
@Builder
public class ExampleDto {

    private Long id;
    private String problem;
    private String answer;
    private String tag;

}
