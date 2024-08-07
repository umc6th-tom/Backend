package umc6.tom.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;
import umc6.tom.inquiry.service.RootQNAService;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/root")
public class RootQNAController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RootQNAService rootQNAService;

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 답변 등록
     */
    @PostMapping("/qna/{qna-id}")
    public ApiResponse<QNAResponseDto.AnswerQNADto> AnswerQNA(@PathVariable("qna-id") Long qnaId,
                                                        @RequestBody @Valid QNARequestDto.AnswerDto request) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootQNAService.answerQNA(request, userId, qnaId));
    }

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 답변 수정
     */
    @PatchMapping("/qna/{qna-id}")
    public ApiResponse<QNAResponseDto.AnswerQNADto> UpdateQNA(@PathVariable("qna-id") Long qnaId,
                                                              @RequestBody @Valid QNARequestDto.AnswerDto request) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootQNAService.answerQNA(request, userId, qnaId));
    }

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 답변 삭제
     */
    @DeleteMapping("/qna/{qna-id}")
    public ApiResponse<QNAResponseDto.DeleteQNAAnswerDto> DeleteQNA(@PathVariable("qna-id") Long qnaId) {
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootQNAService.deleteQNA(userId, qnaId));
    }

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 리스트
     */
    @GetMapping("/qna/list-{status}")
    public ApiResponse<QNAResponseDto.RootQNAListViewListDto> RootQNAList(@PathVariable("status") String status,
                                                                  @RequestParam(name = "page") Integer page) {
        //Long userId = jwtTokenProvider.getUserIdFromToken();


        return ApiResponse.onSuccess(rootQNAService.rootQNAListViewList(4L, status, page));
    }

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 리스트 검색
     */
    @GetMapping("/qna/search/{status}")
    public ApiResponse<QNAResponseDto.RootQNAListViewListDto> RootQNASearchList(@PathVariable("status") String status,
                                                                      @RequestParam(name = "page") Integer page,
                                                                      @RequestParam(name = "content") String content) {
        //Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootQNAService.rootQNASearchList(4L, status, page, content));
    }

    /**
     * 24.08.07 작성자 : 박재락
     * 관리자 문의 글 보기
     */
    @GetMapping("/qna/{qna-id}")
    public ApiResponse<QNAResponseDto.RootQNAViewDto> RootQNAView(@PathVariable("qna-id") Long qnaId){
        //Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(rootQNAService.rootQNAView(4L, qnaId));
    }
}
