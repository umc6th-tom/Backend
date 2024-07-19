package umc6.tom.notice.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.notice.dto.NoticeDto;
import umc6.tom.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 리스트 조회
    //Path = /notice/paging?page=1 , /notice/paging
    //15개씩 나옴, /paging == /paging?page=1 결과가 동일함
    @GetMapping("/paging")
    public ApiResponse<List<NoticeDto>> findAll(@RequestParam(defaultValue = "1") int page,
                                                @PageableDefault(size = 15) Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        Pageable adjustedPageable = PageRequest.of(page - 1, pageable.getPageSize(), pageable.getSort());
        List<NoticeDto> noticeDTOPage = noticeService.findAll(adjustedPageable);

        return ApiResponse.onSuccess(noticeDTOPage);
    }

    //공지사항 글보기
    @GetMapping("/{id}")
    public ApiResponse<NoticeDto> findById(@PathVariable long id) {

        return ApiResponse.onSuccess(noticeService.findNotice(id));
    }





}
