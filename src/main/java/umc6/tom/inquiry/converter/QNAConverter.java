package umc6.tom.inquiry.converter;

import org.springframework.data.domain.Page;
import umc6.tom.board.functionClass.DateCalc;
import umc6.tom.inquiry.dto.QNARequestDto;
import umc6.tom.inquiry.dto.QNAResponseDto;
import umc6.tom.inquiry.model.Inquiry;
import umc6.tom.inquiry.model.InquiryPicture;
import umc6.tom.inquiry.model.enums.Status;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QNAConverter {

    public static Inquiry toQNA(QNARequestDto.RegisterDto request, User user){

        return Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();
    }

    public static InquiryPicture toQNAPicture(Inquiry inquiry, String pic){

        return InquiryPicture.builder()
                .inquiry(inquiry)
                .pic(pic)
                .build();
    }

    public static QNAResponseDto.RegisterResultDto toQNAResult(Inquiry qna){

        return QNAResponseDto.RegisterResultDto.builder()
                .qnaId(qna.getId())
                .createdAt(qna.getCreatedAt())
                .build();
    }

    public static QNAResponseDto.QNAListViewDto toQNAListView(Inquiry inquiry){
        String status = inquiry.getStatus().getAdminKor();
        if(inquiry.getStatus().toString().equals("WAITING"))
            status = "확인 중";
        return QNAResponseDto.QNAListViewDto.builder()
                .id(inquiry.getId())
                .status(status)
                .title(inquiry.title)
                .createdAt(new DateCalc().formatDate2(inquiry.getCreatedAt()))
                .build();
    }

    public static QNAResponseDto.QNAListViewListDto toQNAListViewListDTO(Page<Inquiry> qnaList){
        List<QNAResponseDto.QNAListViewDto> qnaListViewList = qnaList.stream()
                .map(QNAConverter::toQNAListView).collect(Collectors.toList());

        return QNAResponseDto.QNAListViewListDto.builder()
                .isLast(qnaList.isLast())
                .isFirst(qnaList.isFirst())
                .totalPage(qnaList.getTotalPages())
                .totalElements(qnaList.getTotalElements())
                .listSize(qnaListViewList.size())
                .qnaList(qnaListViewList)
                .build();
    }

    public static String toQNAPicture(InquiryPicture qnaPic){

        return qnaPic.getPic();
    }

    public static QNAResponseDto.QNAViewDto toQNAView(Inquiry qna){
        List<String> qnaPicList = qna.getInquiryPictureList().stream()
                .map(QNAConverter::toQNAPicture).toList();

        return QNAResponseDto.QNAViewDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .createdAt(new DateCalc().formatDate2(qna.getCreatedAt()))
                .answer(qna.getAnswer())
                .answeredAt(new DateCalc().formatDate2(qna.getAnsweredAt()))
                .picList(qnaPicList)
                .build();
    }

    public static QNAResponseDto.AnswerQNADto toQNAAnswer (Inquiry qna){

        return QNAResponseDto.AnswerQNADto.builder()
                .qnaId(qna.getId())
                .answer(qna.getAnswer())
                .answeredAdminId(qna.getAdminUserId())
                .answeredAt(qna.getAnsweredAt())
                .build();
    }

    public static QNAResponseDto.DeleteQNAAnswerDto toDeleteQNAAnswer (Inquiry qna){

        return QNAResponseDto.DeleteQNAAnswerDto.builder()
                .qnaId(qna.getId())
                .answeredAdminId(qna.getAdminUserId())
                .deletedAnswerAt(LocalDateTime.now())
                .build();
    }

    public static QNAResponseDto.RootQNAListViewDto toRootQNAListView(Inquiry qna) {
        String korStatus = Status.ANSWERED.getAdminKor();
        if(qna.getStatus().toString().equals("WAITING"))
            korStatus = Status.WAITING.getAdminKor();

        return QNAResponseDto.RootQNAListViewDto.builder()
                .id(qna.getId())
                .status(korStatus)
                .title(qna.getTitle())
                .content(qna.getContent())

                .createdAt(new DateCalc().formatDate2(qna.getCreatedAt()))
                .build();
    }

    public static QNAResponseDto.RootQNAListViewListDto toRootQNAListViewList(Page<Inquiry> qNAList) {
        List<QNAResponseDto.RootQNAListViewDto> rootQNAListViewList = qNAList.stream()
                .map(QNAConverter::toRootQNAListView).collect(Collectors.toList());

        return QNAResponseDto.RootQNAListViewListDto.builder()
                .isLast(qNAList.isLast())
                .isFirst(qNAList.isFirst())
                .totalPage(qNAList.getTotalPages())
                .totalElements(qNAList.getTotalElements())
                .listSize(rootQNAListViewList.size())
                .rootQNAList(rootQNAListViewList)
                .build();
    }

    public static QNAResponseDto.RootQNAViewDto toRootQNAView(Inquiry qna) {
        List<String> qnaPic = new ArrayList<>();
        for(InquiryPicture inquiryPicture :qna.getInquiryPictureList())
            qnaPic.add(inquiryPicture.getPic());

        return QNAResponseDto.RootQNAViewDto.builder()
                .id(qna.getId())
                .userId(qna.getUser().getId())
                .userNickname(qna.getUser().getNickName())
                .userprofile(qna.getUser().getPic())
                .createdAt(new DateCalc().boardListDate(qna.getCreatedAt()))
                .title(qna.getTitle())
                .content(qna.getContent())
                .picList(qnaPic)
                .answer(qna.getAnswer())
                .build();
    }
}
