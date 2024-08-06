package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.MajorHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.dto.GptRes;
import umc6.tom.gpt.dto.MajorReq;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

import java.util.Optional;

import static umc6.tom.gpt.function.Function.extractContent;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final ExampleRepository exampleRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

//    public AnswerDto find(String questions) {
//        //GPT 한테 물어보고 가져오고 DB에 저장을 시키고 저장한 데이터를 리턴해주기
////        answerRepository.save()
//
//    }


    //답변예제 보내기
    public ExampleDto exampleFindId(long id) {
        Optional<Example> optionalExample = exampleRepository.findById(id);
        if (optionalExample.isPresent()) {
            Example example = optionalExample.get();
            ExampleDto exampleDto = ExampleConverter.toDto2(example);
            return exampleDto;
        }
        else {
            return null;
        }
    }


    public GptRes.responseText findMajor(MajorReq.SearchDto searchDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //GPT 기본 설정
//        GptReq request = new GptReq(
//                model, searchDto.getQuestion(), 1,2048,0.7,0,0, user);
//        GptRes gptResponse = restTemplate.postForObject(
//                apiUrl
//                , request
//                , GptRes.class
//        );

        //GPT 답변 파싱  이후에 활성화하기 -> gptResponse.getChoices().get(0).getMessage().getContent();
        String responseText = " 답변: 자바(Java)는 객체 지향 프로그래밍 언어로, 높은 이식성과 강력한 보안 기능을 갖추고 있습니다. 자바는 \\\"Write Once, Run Anywhere\\\"의 모토를 가지고 있어, 한 번 작성된 코드가 다양한 플랫폼에서 실행될 수 있습니다. 주요 특징으로는 자동 메모리 관리(가비지 컬렉션), 멀티스레딩, 풍부한 API 등이 있습니다. 자바는 웹 애플리케이션, 모바일 애플리케이션(안드로이드), 엔터프라이즈 솔루션 등에 널리 사용됩니다.\n\n예시 질문:\n자바의 주요 특징 중 하나인 가비지 컬렉션(Garbage Collection)이란 무엇인가?\n\n정답:\n가비지 컬렉션(Garbage Collection)이란 프로그램이 더 이상 사용하지 않는 메모리 공간을 자동으로 해제하여 메모리 누수를 방지하고 시스템 자원을 효율적으로 관리하는 기능을 말합니다. 자바는 개발자가 명시적으로 메모리를 해제하지 않아도 되도록 자동 메모리 관리를 지원합니다";
        String answer = extractContent(responseText, "답변:");
        String exampleQuestion = extractContent(responseText, "예시 질문:");
        String correctAnswer = extractContent(responseText, "정답:");

//      이후에 활성화하기  answerRepository.save(ExampleConverter.toAnswerEntity(searchDto.getQuestion(), answer, user));
        answerRepository.save(ExampleConverter.toAnswerEntity("자바", answer, user));

//      이후에 활성화하기  return new GptRes.responseText(searchDto.getQuestion(), answer,exampleQuestion,correctAnswer);
        return new GptRes.responseText("자바", answer,exampleQuestion,correctAnswer);
    }

    public ExampleDto exampleRegister(MajorReq.exampleRegisterDto exampleDto) {
        Long exampleId = exampleRepository.save(ExampleConverter.toExampleEntity(exampleDto)).getId();

        return ExampleConverter.toDto2(exampleRepository.findById(exampleId).orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND)));
    }
}
