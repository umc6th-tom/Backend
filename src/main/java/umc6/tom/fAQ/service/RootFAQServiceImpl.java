package umc6.tom.fAQ.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.FAQHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.fAQ.converter.FAQConverter;
import umc6.tom.fAQ.dto.FAQRequestDto;
import umc6.tom.fAQ.dto.FAQResponseDto;
import umc6.tom.fAQ.model.FAQ;
import umc6.tom.fAQ.repository.FAQRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.model.enums.Role;
import umc6.tom.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RootFAQServiceImpl implements RootFAQService{

    private final FAQRepository fAQRepository;
    private final UserRepository userRepository;

    @Override
    public FAQResponseDto.RootFAQAddDto addRootFAQ(FAQRequestDto.AddFAQDto request,Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //관리자 검증
        if(!user.getRole().equals(Role.ADMIN))
            throw new UserHandler(ErrorStatus._FORBIDDEN);
        FAQ newFaq = FAQConverter.toFAQ(request, user);

        fAQRepository.save(newFaq);

        return FAQConverter.toAddRootFAQ(newFaq);
    }

    @Override
    @Transactional
    public FAQResponseDto.RootFAQAddDto updateRootFAQ(FAQRequestDto.AddFAQDto request, Long userId, Long fqaId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //관리자 검증
        if(!user.getRole().equals(Role.ADMIN))
            throw new UserHandler(ErrorStatus._FORBIDDEN);
        FAQ faq = fAQRepository.findById(fqaId).orElseThrow(() -> new FAQHandler(ErrorStatus.FAQ_NOT_FOUND));

        faq.setTitle(request.getTitle());
        faq.setCategory(request.getCategory());
        faq.setContent(request.getContent());
        FAQ updatedFAQ =fAQRepository.save(faq);

        return FAQConverter.toAddRootFAQ(updatedFAQ);
    }

    @Override
    @Transactional
    public FAQResponseDto.RootFAQDeleteDto deleteRootFAQ(Long userId, Long fqaId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //관리자 검증
        if (!user.getRole().equals(Role.ADMIN))
            throw new UserHandler(ErrorStatus._FORBIDDEN);
        fAQRepository.deleteById(fqaId);

        return FAQConverter.toDeleteRootFAQ(fqaId);
    }
}
