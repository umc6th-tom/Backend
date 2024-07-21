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
import umc6.tom.gpt.service.MajorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/major")
public class MajorController {

    private final MajorService majorService;

    //전공 검색하기
//    @GetMapping("/find")
//    public ApiResponse<AnswerDto> find(@RequestParam("questions") String questions){

//        AnswerDto dto = majorService.find(questions);
//        return ApiResponse.onSuccess(dto);
//    }

    //예제로 글 작성창 가기 , 예제 데이터 뿌려주기!
    @GetMapping("{id}")
    public ApiResponse<ExampleDto> getExampleById(@PathVariable long id) {

            ExampleDto exampleDto = majorService.exampleFindId(id);
            return ApiResponse.onSuccess(exampleDto);

    }

}
