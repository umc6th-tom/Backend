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
import umc6.tom.user.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExampleConverter {

    //예제 조회
    public ExampleDto toDto(Example entity){
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
                .question(answer.getQuestion())
                .content(answer.getContent())
                .createdAt(DateCalc.formatDate2(answer.getCreatedAt()))
                .nickname(answer.getUser().getNickName())
                .major(answer.getMajors().getMajor())
                .majorId(answer.getMajors().getId())
                .build();
    }
}
