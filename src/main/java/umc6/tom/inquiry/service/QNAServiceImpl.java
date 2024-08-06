package umc6.tom.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.QNAHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.common.model.Uuid;
import umc6.tom.common.repository.UuidRepository;
import umc6.tom.config.AmazonConfig;
import umc6.tom.inquiry.converter.QNAConverter;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;
import umc6.tom.inquiry.model.Inquiry;
import umc6.tom.inquiry.repository.InquiryPictureRepository;
import umc6.tom.inquiry.repository.InquiryRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;
import umc6.tom.util.AmazonS3Util;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QNAServiceImpl implements QNAService{

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryPictureRepository inquiryPictureRepository;
    private final AmazonConfig amazonConfig;
    private final AmazonS3Util amazonS3Util;
    private final UuidRepository uuidRepository;

    @Override
    public QNAResponseDto.RegisterResultDto registerQna(QNARequestDto.RegisterDto request, Long userId, MultipartFile[] files) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));


        Inquiry qna =inquiryRepository.save(QNAConverter.toQNA(request, user));

        if (!ObjectUtils.isEmpty(files)) {
            String fileName = null;
            for (MultipartFile file : files) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                    fileName = amazonS3Util.upload(file, amazonConfig.getInquiryPath(), savedUuid);
                } catch (IOException e) {
                    throw new QNAHandler(ErrorStatus.QNA_FILE_UPLOAD_FAILED);
                }
                inquiryPictureRepository.save(QNAConverter.toQNAPicture(qna, fileName));
            }
        }
        return QNAConverter.toQNAResult(qna);
    }

    @Override
    public QNAResponseDto.QNAListViewListDto QnaListView(Long userId, Integer page) {
        Page<Inquiry> qnaPage = inquiryRepository.findAllByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, 12));
        return QNAConverter.toQNAListViewListDTO(qnaPage);
    }

    @Override
    public QNAResponseDto.QNAViewDto QnaView(Long userId, Long qnaId) {
        Inquiry qna = inquiryRepository.findById(qnaId).orElseThrow(() -> new QNAHandler(ErrorStatus.QNA_NOT_FOUND));

        if(!Objects.equals(userId, qna.getUser().getId()))
            throw new QNAHandler(ErrorStatus._FORBIDDEN);

        return QNAConverter.toQNAView(qna);
    }
}
