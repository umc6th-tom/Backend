package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.dto.GptReq;
import umc6.tom.gpt.dto.GptRes;
import umc6.tom.gpt.dto.MajorReq;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final AnswerRepository answerRepository;
    private final ExampleRepository exampleRepository;
    private final ExampleFavoriteRepository exampleFavoriteRepository;
    private final ExampleConverter exampleConverter;
    private final UserRepository userRepository;

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


    public String findMajor(MajorReq.SearchDto searchDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        GptReq request = new GptReq(
                model, searchDto.getQuestion(), 0.8,2048,1,0,0);

        GptRes gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GptRes.class
        );

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
