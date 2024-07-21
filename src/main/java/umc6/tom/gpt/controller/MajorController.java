package umc6.tom.gpt.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.gpt.dto.AnswerDto;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.service.FavoriteService;
import umc6.tom.gpt.service.MajorService;
import umc6.tom.security.JwtTokenProvider;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/major")
public class MajorController {

    private final MajorService majorService;
    private final FavoriteService favoriteService;
    private final JwtTokenProvider jwtTokenProvider;

    //전공 검색하기
//    @GetMapping("/find")
//    public ApiResponse<AnswerDto> find(@RequestParam("questions") String questions){

//        AnswerDto dto = majorService.find(questions);
//        return ApiResponse.onSuccess(dto);
//    }

    //예제로 글 작성창 가기 , 예제 데이터 뿌려주기!
    @GetMapping("/{id}")
    public ApiResponse<ExampleDto> getExampleById(@PathVariable long id) {

            return ApiResponse.onSuccess(majorService.exampleFindId(id));
    }

    //즐겨찾기 조회
    @GetMapping("/myfavorite")
    public ApiResponse<List<ExampleDto>> getFindAllFavorite() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(favoriteService.findAllFavorite(userId));

    }

    //즐겨찾기 등록
//    @PostMapping("/register")
//    public ApiResponse<ExampleDto> registerNewMajor(@RequestBody ExampleDto exampleDto) {
//        return
//    }

//    //즐겨찾기 제거 후 조회
//    @DeleteMapping("/{id}")
//    public ApiResponse<ExampleDto> deleteFavorite(@PathVariable long id) {
//        Long userId = jwtTokenProvider.getUserIdFromToken();
//        favoriteService.deleteById(id);
//        favoriteService.findAllFavorite(userId);
//
//        return ApiResponse.onSuccess();
//    }


}
