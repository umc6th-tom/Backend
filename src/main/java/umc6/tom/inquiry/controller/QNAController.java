package umc6.tom.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;
import umc6.tom.inquiry.service.QNAService;
import umc6.tom.security.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/qna")
public class QNAController {

    private final JwtTokenProvider jwtTokenProvider;
    private final QNAService qnaService;

    /**
     * 24.08.06 작성자 : 박재락
     * 문의 등록
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<QNAResponseDto.RegisterResultDto> QNARegister(@RequestPart @Valid QNARequestDto.RegisterDto request,
                                                                     @RequestPart(required = false) MultipartFile[] files){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(qnaService.registerQna(request, userId, files));
    }

    /**
     * 24.08.06 작성자 : 박재락
     * 문의 리스트 조회
     */
    @GetMapping("/my-list")
    public ApiResponse<QNAResponseDto.QNAListViewListDto> QNAList(@RequestParam(name = "page") Integer page){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(qnaService.QnaListView(userId, page));
    }

    /**
     * 24.08.06 작성자 : 박재락
     * 문의 상세 내용 조회
     */
    @GetMapping("/{qna-id}")
    public ApiResponse<QNAResponseDto.QNAViewDto> QnaView(@PathVariable(name = "qna-id") Long qnaId){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(qnaService.QnaView(userId, qnaId));
    }
}
