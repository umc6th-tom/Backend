package umc6.tom.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.gpt.converter.ExampleConverter;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.model.ExampleFavorite;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;

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


    //유저 ID로 즐겨찾기 조회해서 문제 ID 알아오기
    public List<ExampleDto> findAllFavorite(Long userId){
        List<Long> exampleIdList = exampleFavoriteRepository.findExampIdsByUserIdOrderByCreatedAtAsc(userId);
        List<Example> exampleEntity = exampleRepository.findAllById(exampleIdList);

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
