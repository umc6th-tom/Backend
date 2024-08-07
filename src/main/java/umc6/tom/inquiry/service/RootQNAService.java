package umc6.tom.inquiry.service;

import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;

public interface RootQNAService {

    QNAResponseDto.AnswerQNADto answerQNA(QNARequestDto.AnswerDto request, Long userId, Long qnaId);

    QNAResponseDto.DeleteQNAAnswerDto deleteQNA(Long userId, Long qnaId);

}
