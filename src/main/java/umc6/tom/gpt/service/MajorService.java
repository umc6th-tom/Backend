package umc6.tom.gpt.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.AnswerDto;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.notice.dto.NoticeDto;
import umc6.tom.notice.dto.NoticePictureDto;
import umc6.tom.notice.model.Notice;
import umc6.tom.notice.model.NoticePicture;
import umc6.tom.notice.repository.NoticePictureRepository;
import umc6.tom.notice.repository.NoticeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            ExampleDto exampleDto = ExampleConverter.toDto(example);
            return exampleDto;
        }
        else {
            return null;
        }
    }


}
