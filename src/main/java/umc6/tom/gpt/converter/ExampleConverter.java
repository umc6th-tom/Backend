package umc6.tom.gpt.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.common.model.Majors;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.dto.MajorReq;
import umc6.tom.gpt.dto.MajorRes;
import umc6.tom.gpt.model.Answer;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.model.ExampleFavorite;
import umc6.tom.user.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExampleConverter {

    //예제 조회
    public static ExampleDto toDto(Example entity){
        return ExampleDto.builder()
                .id(entity.getId())
                .problem(entity.getProblem())
                .answer(entity.getAnswer())
                .tag(entity.getTag())
                .build();
    }

    //예제 조회
    public static ExampleDto toDto2(Example entity){
        return ExampleDto.builder()
                .id(entity.getId())
                .problem(entity.getProblem())
                .answer(entity.getAnswer())
                .tag(entity.getTag())
                .build();
    }

    public static Example toEntity(ExampleDto exampleDto) {
        return Example.builder()
                .id(exampleDto.getId())
                .problem(exampleDto.getProblem())
                .answer(exampleDto.getAnswer())
                .tag(exampleDto.getTag())
                .build();
    }

    public static Example toExampleEntity(MajorReq.exampleRegisterDto responseDto) {
        return Example.builder()
                .problem(responseDto.getExampleQuestion())
                .answer(responseDto.getCorrectAnswer())
                .tag(responseDto.getTag())
                .build();
    }

    public static Answer toAnswerEntity(String question, String content, User user, Majors major){
        return Answer.builder()
                .majors(major)
                .question(question)
                .content(content)
                .user(user)
                .build();
    }

    public static MajorRes.getHome getHome(Answer answer){
        return MajorRes.getHome.builder()
                .userId(answer.getUser().getId())
                .answerId(answer.getId())
                .majorId(answer.getMajors().getId())
                .question(answer.getQuestion())
                .content(answer.getContent())
                .nickname(answer.getUser().getNickName())
                .major(answer.getMajors().getMajor())
                .createdAt(DateCalc.formatDate2(answer.getCreatedAt()))
                .build();
    }

    public static MajorRes.ExampleResDto favoriteDto(MajorRes.ExampleDto exampleDto, ExampleFavorite favorite){
        return MajorRes.ExampleResDto.builder()
                .exampleId(exampleDto.getId())
                .favoriteId(favorite.getId())
                .answerId(exampleDto.getAnswerEntity().getId())
                .problem(exampleDto.getProblem())
                .answer(exampleDto.getAnswer())
                .tag(exampleDto.getTag())
                .build();
    }

    public static MajorRes.ExampleDto ExampleDto(Example example){
        return MajorRes.ExampleDto.builder()
                .id(example.getId())
                .answerEntity(example.getAnswerId())
                .problem(example.getProblem())
                .answer(example.getAnswer())
                .tag(example.getTag())
                .build();
    }
}
