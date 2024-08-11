package umc6.tom.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.alarm.model.enums.AlarmOnOff;
import umc6.tom.alarm.model.enums.Category;
import umc6.tom.alarm.repository.AlarmSetRepository;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.apiPayload.exception.handler.AlarmSetHandler;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.PinHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.converter.BoardConverter;
import umc6.tom.board.model.Board;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.converter.PinComplaintConverter;
import umc6.tom.comment.converter.PinConverter;
import umc6.tom.comment.converter.PinLikeConverter;
import umc6.tom.comment.converter.PinPictureConverter;
import umc6.tom.comment.dto.PinReportReqDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.*;
import umc6.tom.comment.model.enums.PinBoardStatus;
import umc6.tom.comment.repository.*;
import umc6.tom.common.model.Uuid;
import umc6.tom.common.repository.UuidRepository;
import umc6.tom.config.AmazonConfig;
import umc6.tom.firebase.service.PushMessage;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;
import umc6.tom.util.AmazonS3Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PinService {

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final PinPictureRepository pinPictureRepository;
    private final PinLikeRepository pinLikeRepository;
    private final PinComplaintRepository pinComplaintRepository;
    private final PinComplaintPictureRepository pinComplaintPictureRepository;
    private final PushMessage pushMessage;
    private final AlarmSetRepository alarmSetRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Util amazonS3Util;
    private final AmazonConfig amazonConfig;

    //댓글 등록
    @Transactional
    public ApiResponse pinRegister(PinReqDto.PinCommentAndPic pinReq, Long boardId, Long userId, MultipartFile[] files) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //게시판 댓글 저장
        Board board =  boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARDLIKE_NOT_FOUND));
        Pin pin = PinConverter.toPinEntity(user, board, pinReq);
        // 빈 파일 필터링. 안하면 파일 없어도 length값 1 됨
        files = (files != null && files.length > 0) ?
                Arrays.stream(files)
                        .filter(file -> !file.isEmpty())
                        .toArray(MultipartFile[]::new) :
                new MultipartFile[0];

        if (!org.springframework.util.ObjectUtils.isEmpty(files)) {
            if(files.length>3)
                throw new BoardHandler(ErrorStatus.BOARD_PICTURE_OVERED);

            String fileName = null;
            for (MultipartFile file : files) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                    fileName = amazonS3Util.upload(file, amazonConfig.getPinPath(), savedUuid);
                } catch (IOException e) {
                    throw new BoardHandler(ErrorStatus.BOARD_FILE_UPLOAD_FAILED);
                }
                //fileName이 들어가야함
                PinPicture newPinPicture = PinConverter.toPinPicture(pin, fileName);
                pinPictureRepository.save(newPinPicture);
            }
        }
        Pin pinSaved = pinRepository.save(pin);

        //댓글 사진 테이블에 저장하기
//        try{
//            for(String picUrl : pinReq.getPic()){
//                PinPicture pinPicture = PinPictureConverter.toPinPictureEntity(picUrl, pinSaved);
//                pinPictureRepository.save(pinPicture);
//            }
//        }catch(Exception e){
//            throw new PinHandler(ErrorStatus.PIN_NOT_REGISTER);
//        }

        //댓글+대댓글 20개 도달시 핫글
        if(ObjectUtils.isEmpty(board.getPopularAt()) && BoardConverter.toPinAndCommentCount(board.getPinList()) >= 20){
            board.setPopularAt(LocalDateTime.now());
            boardRepository.updateBoardPopularAt(boardId, LocalDateTime.now());
        }

        // 댓글 알림 유무
        AlarmSet alarmSet = alarmSetRepository.findByUserId(board.getUser().getId()).orElseThrow(()
                -> new AlarmSetHandler(ErrorStatus.ALARM_SET_NOT_FOUND));
        //댓글 알림 보내기
        if (alarmSet.getPinSet().equals(AlarmOnOff.ON) || !user.getId().equals(board.getUser().getId()))
            pushMessage.commentNotification(board.getUser(), board, board.getTitle(), Category.WrittenBoard);


        return ApiResponse.onSuccess(200);
    }

    //댓글 수정하기위해 데이터 주는거!!
    @Transactional
    public ApiResponse getDetailPin(Long commentId) {
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
        PinResDto.DetailPin pinResDto = PinConverter.toPinDto(pin);

        return ApiResponse.onSuccess(pinResDto);
    }

    @Transactional
    public ApiResponse pinModify(PinReqDto.PinAndPic pinDto, MultipartFile[] files) {
        try {
            Pin existingPin = pinRepository.findById(pinDto.getId())
                    .orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

            if (pinDto.getComment() != null) {
                existingPin.setComment(pinDto.getComment());
            }
            Pin pinSaved = pinRepository.save(existingPin);
            pinPictureRepository.deleteAllByPin(pinSaved);

            for (String picUrl : pinDto.getPic()) {
                PinPicture pinPicutreEntity = PinPictureConverter.toPinPictureEntity(picUrl, pinSaved);
                pinPictureRepository.save(pinPicutreEntity);
            }

            return ApiResponse.onSuccess(200);
        }
        catch(Exception e){
            throw new PinHandler(ErrorStatus.PIN_NOT_UPDATE);
        }
    }

    public ApiResponse pinDelete(Long commentId) {
        try {
            pinRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new PinHandler(ErrorStatus.PIN_NOT_DELETE);
        }
        return ApiResponse.onSuccess(200);
    }

    //댓글 좋아요 추가/제거
    public ApiResponse pinLikeSet(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        PinLike likeEntity = pinLikeRepository.findByUser(user);

        if(Objects.equals(user.getId(), pin.getUser().getId())){
            return ApiResponse.onFailure("PIN_LIKE_4010","자기 댓글에 좋아요를 누를 수 없습니다.", null);
        }else {
            if (likeEntity == null) {
                pinLikeRepository.save(PinLikeConverter.toEntity(user, pin));
                pushMessage.commentLikeNotification(pin.getBoard(), pin.getUser(), pin.getComment(), user);
                return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_LIKE);
            } else {
                pinLikeRepository.delete(likeEntity);
                return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_UNLIKE);
            }
        }
    }

    //댓글 신고하기
    public ApiResponse pinReport(Long commentId, PinReportReqDto.PinReportDto reportDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        if (Objects.equals(user.getId(), pin.getUser().getId())) {
            return ApiResponse.onFailure("PIN_REPORT_4011", "자기 댓글은 신고를 할 수 없습니다.", null);
        }else{
            pin.setReport(pin.getReport() + 1);

            if(pin.getReport() == 10) {
                pin.setPinBoardStatus(PinBoardStatus.OVERCOMPLAINT);
            }
            pinRepository.save(pin);

            PinComplaint pinComplaintEntity = PinComplaintConverter.toPinComplaintEntity(reportDto, user, pin);
            PinComplaint pinComplaintSaved = pinComplaintRepository.save(pinComplaintEntity);

            for (String pic : reportDto.getPic()) {
                pinComplaintPictureRepository.save(PinComplaintPicture.builder().pinComplaint(pinComplaintSaved).pic(pic).build());
            }

            User pinUser = userRepository.findById(pin.getUser().getId()).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
            pinUser.setReport(pinUser.getReport() + 1);
            userRepository.save(pinUser);

            return ApiResponse.onSuccess(200);
        }
    }
}
