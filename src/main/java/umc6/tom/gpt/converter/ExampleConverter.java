package umc6.tom.gpt.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExampleConverter {

    //예제 조회
    public static ExampleDto toDto(Example entity){
//        ExampleDto exampleDto = new ExampleDto();
//        exampleDto.setId(exampleEntity.getId());
//        exampleDto.setAnswer(exampleEntity.getAnswer());
//        exampleDto.setProblem(exampleEntity.getProblem());
//        exampleDto.setTag(exampleEntity.getTag());
        return ExampleDto.builder()
                .id(entity.getId())
                .problem(entity.getProblem())
                .answer(entity.getAnswer())
                .tag(entity.getTag())
                .build();
    }
}
