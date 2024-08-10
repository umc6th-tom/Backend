package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.MajorHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.dto.GptReq;
import umc6.tom.gpt.dto.GptRes;
import umc6.tom.gpt.dto.MajorReq;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;
import umc6.tom.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static umc6.tom.gpt.function.Function.extractContent;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final ExampleRepository exampleRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private static final Logger log = LoggerFactory.getLogger(MajorService.class);
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

////        GPT 기본 설정
        GptReq request = new GptReq(
                model, searchDto.getQuestion(), 1.5,2048,0.7,0,0, user);
        GptRes gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GptRes.class
        );

        //GPT 답변 파싱  이후에 활성화하기 -> gptResponse.getChoices().get(0).getMessage().getContent();
        String responseText = gptResponse.getChoices().get(0).getMessage().getContent();
        log.info(responseText);
        List<String> exampleKeywords = List.of("예시문제:", "예시 문제:", "예시 질문:", "예시질문:" , "예시문제 :", "예시 문제 :", "예시 질문 :", "예시질문 :");
        String answer = extractContent(responseText, "답변:");
        String exampleQuestion = extractContent(responseText, "예시문제:");
        String correctAnswer = extractContent(responseText, "정답:");

//      이후에 활성화하기  answerRepository.save(ExampleConverter.toAnswerEntity(searchDto.getQuestion(), answer, user));
            answerRepository.save(ExampleConverter.toAnswerEntity(searchDto.getQuestion(), answer, user));

//      이후에 활성화하기  return new GptRes.responseText(searchDto.getQuestion(), answer,exampleQuestion,correctAnswer);
        return new GptRes.responseText(searchDto.getQuestion(), answer,exampleQuestion,correctAnswer);
    }

    public ExampleDto exampleRegister(MajorReq.exampleRegisterDto exampleDto) {
        Long exampleId = exampleRepository.save(ExampleConverter.toExampleEntity(exampleDto)).getId();

        return ExampleConverter.toDto2(exampleRepository.findById(exampleId).orElseThrow(() -> new MajorHandler(ErrorStatus.MAJORS_NOR_FOUND)));
    }
}
