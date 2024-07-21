package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.model.ExampleFavorite;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final AnswerRepository answerRepository;
    private final ExampleRepository exampleRepository;
    private final ExampleFavoriteRepository exampleFavoriteRepository;

    private final ExampleConverter exampleConverter;
    private final UserRepository userRepository;


    //유저 ID로 즐겨찾기 조회해서 문제 ID 알아오기
    public List<ExampleDto> findAllFavorite(Long userId){
        //매개변수를 통해 jwt 헤더 id값을 -> user id 바꾸기
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //user 값으로 즐겨찾기 리스트 가져오기
        List<ExampleFavorite> exampleList = exampleFavoriteRepository.findExampleFavoritesByUserOrderByCreatedAtAsc(user);

        List<Long> exampleId = exampleList.stream()
                                            .map(ExampleFavorite -> ExampleFavorite.getExample().getId())
                                            .toList();

        List<Example> exampleEntity = exampleRepository.findAllById(exampleId);

        return exampleEntity.stream()
                        .map(exampleConverter::toDto)
                        .collect(Collectors.toList());
    }

//    //즐겨찾기 제거
//    public ExampleDto deleteById(long id) {
//        Optional<ExampleFavorite> exampleEntity = exampleFavoriteRepository.findById(id);
//
//        Optional<Long> exampleId = exampleEntity.map(exampleFavorite -> exampleFavorite.getExample().getId());
//
//        exampleRepository.deleteById(ExampleId);
//
//
//    }
}
