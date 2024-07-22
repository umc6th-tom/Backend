package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final AnswerRepository answerRepository;
    private final ExampleRepository exampleRepository;
    private final ExampleFavoriteRepository exampleFavoriteRepository;
    private final ExampleConverter exampleConverter;



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


}
