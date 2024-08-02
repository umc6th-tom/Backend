package umc6.tom.gpt.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.MajorHandler;
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
    @Transactional
    public List<ExampleDto> findAllFavorite(Long userId) {
        //매개변수를 통해 jwt 헤더 id값을 -> user id 바꾸기
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //user 값으로 즐겨찾기 리스트 가져오기
        List<ExampleFavorite> exampleList = exampleFavoriteRepository.findExampleFavoritesByUserOrderByCreatedAtAsc(user);

        List<Long> exampleId = exampleList.stream()
                .map(ExampleFavorite -> ExampleFavorite.getExample().getId())
                .collect(Collectors.toList());

        List<Example> exampleEntity = exampleRepository.findAllById(exampleId);

        return exampleEntity.stream()
                .map(exampleConverter::toDto)
                .collect(Collectors.toList());
    }


    //즐겨찾기 제거
    @Transactional
    public ApiResponse<Integer> deleteById(long id) {
        try {
            Optional<ExampleFavorite> exampleEntity = exampleFavoriteRepository.findById(id);
            Long exampleId = exampleEntity.map(exampleFavorite -> exampleFavorite.getExample().getId())
                    .orElseThrow(() -> new MajorHandler(ErrorStatus.EXAMPLE_NOT_FOUND));

            exampleFavoriteRepository.deleteById(id);
            exampleRepository.deleteById(exampleId);

        } catch (Exception e) {
            throw new MajorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
        return ApiResponse.onSuccess(200);
    }

    //즐겨찾기 등록
    public ApiResponse<Integer> save(Long userId, long exampleId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
            Example exampleOptionalEntity = exampleRepository.findById(exampleId)
                    .orElseThrow(() -> new MajorHandler(ErrorStatus.EXAMPLE_NOT_FOUND));
            exampleFavoriteRepository.save(ExampleFavorite.createExampleFavorite(exampleOptionalEntity, user));

            return ApiResponse.onSuccess(200);
        } catch (Exception e) {
            throw new MajorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

    }

    public ApiResponse<ExampleDto> getFindById(long id) {

            ExampleFavorite exampleFavoriteEntity = exampleFavoriteRepository.findById(id).orElseThrow(() -> new MajorHandler(ErrorStatus.FAVORITE_NOT_FOUND));
            Example exampleEntity = exampleRepository.findById(exampleFavoriteEntity.getExample().getId()).orElseThrow(() -> new MajorHandler(ErrorStatus.EXAMPLE_NOT_FOUND));
            ExampleDto exampleDto = exampleConverter.toDto(exampleEntity);

            return ApiResponse.onSuccess(exampleDto);

    }
}
