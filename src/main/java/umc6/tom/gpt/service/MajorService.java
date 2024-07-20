package umc6.tom.gpt.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc6.tom.gpt.repository.AnswerRepository;
import umc6.tom.gpt.repository.ExampleFavoriteRepository;
import umc6.tom.gpt.repository.ExampleRepository;
import umc6.tom.notice.dto.NoticeDto;
import umc6.tom.notice.dto.NoticePictureDto;
import umc6.tom.notice.model.Notice;
import umc6.tom.notice.model.NoticePicture;
import umc6.tom.notice.repository.NoticePictureRepository;
import umc6.tom.notice.repository.NoticeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final AnswerRepository answerRepository;
    private final ExampleRepository exampleRepository;
    private final ExampleFavoriteRepository exampleFavoriteRepository;


}
