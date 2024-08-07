package umc6.tom.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.QNAHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.inquiry.converter.QNAConverter;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;
import umc6.tom.inquiry.model.Inquiry;
import umc6.tom.inquiry.model.enums.Status;
import umc6.tom.inquiry.repository.InquiryRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Role;
import umc6.tom.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RootQNAServiceImpl implements RootQNAService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    public QNAResponseDto.AnswerQNADto answerQNA(QNARequestDto.AnswerDto request, Long userId, Long qnaId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //관리자 검증
        if(!user.getRole().equals(Role.ADMIN))
            throw new UserHandler(ErrorStatus._FORBIDDEN);

        Inquiry qna = inquiryRepository.findById(qnaId).orElseThrow(() -> new QNAHandler(ErrorStatus.QNA_NOT_FOUND));

        qna.setAnswer(request.getAnswer());
        qna.setAnsweredAt(LocalDateTime.now());
        qna.setAdminUserId(user.getId());
        qna.setStatus(Status.ANSWERED);
        inquiryRepository.save(qna);

        return QNAConverter.toQNAAnswer(qna);
    }

    @Override
    public QNAResponseDto.DeleteQNAAnswerDto deleteQNA(Long userId, Long qnaId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //관리자 검증
        if(!user.getRole().equals(Role.ADMIN))
            throw new UserHandler(ErrorStatus._FORBIDDEN);

        Inquiry qna = inquiryRepository.findById(qnaId).orElseThrow(() -> new QNAHandler(ErrorStatus.QNA_NOT_FOUND));
        QNAResponseDto.DeleteQNAAnswerDto qnaAnswerDto = QNAConverter.toDeleteQNAAnswer(qna);
        qna.setAnswer(null);
        qna.setAnsweredAt(null);
        qna.setAdminUserId(null);
        qna.setStatus(Status.WAITING);
        inquiryRepository.save(qna);
        return qnaAnswerDto;
    }
}
