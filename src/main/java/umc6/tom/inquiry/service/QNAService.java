package umc6.tom.inquiry.service;

import org.springframework.web.multipart.MultipartFile;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;

public interface QNAService {

    QNAResponseDto.RegisterResultDto registerQna(QNARequestDto.RegisterDto request, Long userId, MultipartFile[] files);

    QNAResponseDto.QNAListViewListDto QnaListView(Long userId, Integer page);

    QNAResponseDto.QNAViewDto QnaView(Long userId, Long qnaId);
}
