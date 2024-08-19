package umc6.tom.gpt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.gpt.dto.ExampleDto;
import umc6.tom.gpt.dto.GptRes;
import umc6.tom.gpt.dto.MajorReq;
import umc6.tom.gpt.dto.MajorRes;
import umc6.tom.gpt.service.FavoriteService;
import umc6.tom.gpt.service.MajorService;
import umc6.tom.security.JwtTokenProvider;
import umc6.tom.user.dto.UserDtoRes;

import java.nio.file.Path;
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

    //즐겨찾기 조회
    @GetMapping("/myfavorite")
    public ApiResponse<List<MajorRes.ExampleResDto>> getFindAllFavorite() {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(favoriteService.findAllFavorite(userId));

    }

    //즐겨찾기 등록
    @PostMapping("/{exampleId}")
    public ApiResponse registerNewFavorite(@PathVariable("exampleId") long exampleId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return favoriteService.save(userId, exampleId);
    }

    //즐겨찾기 제거
    //제거할 때 문제테이블에서도 같이 제거해주기
    //성공시 200 반환
    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteFavorite(@PathVariable long id) {

            return favoriteService.deleteById(id);
    }

    //즐겨찾기에서 예제 보기 즐겨찾기 ID로 검색함 예제데이터 넘겨주기
    @GetMapping("/{favoriteId}/detail")
    public ApiResponse<ExampleDto> getFavoriteById(@PathVariable("favoriteId") long id) {

        return favoriteService.getFindById(id);
    }

    //전공 검색하기(GPT 활용)
    @PostMapping("/find")
    public ApiResponse<GptRes.responseText> findMajor(@RequestBody MajorReq.SearchDto searchDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(majorService.findMajor(searchDto, userId));
    }

    //예제 보기
    @GetMapping("/example/{exampleId}")
    public ApiResponse<MajorRes.ExampleAndAnswerDto> exampleGet(@PathVariable("exampleId") long exampleId) {

        return ApiResponse.onSuccess(majorService.exampleGet(exampleId));
    }

    //예제로 글 작성창 가기 , 예제 데이터 뿌려주기!
    @GetMapping("/{id}")
    public ApiResponse<ExampleDto> getExampleById(@PathVariable long id) {

        return ApiResponse.onSuccess(majorService.exampleFindId(id));
    }

    /**
     * 24.08.16 작성자 : 서정호
     *  홈화면
     */
    @GetMapping("/main")
    public ApiResponse<List<MajorRes.getHome>> getHome(){
        return ApiResponse.onSuccess(majorService.getHome());
    }

    /**
     * 24.08.16 작성자 : 서정호
     *  메인화면에서 질문글 보기
     */
    @GetMapping("/answer/{answerId}")
    public ApiResponse<MajorRes.AnswerDto> getAnswer(@PathVariable("answerId") Long id){
        return ApiResponse.onSuccess(majorService.getAnswer(id));
    }
}
