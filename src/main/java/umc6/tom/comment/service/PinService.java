package umc6.tom.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import umc6.tom.board.model.BoardPicture;
import umc6.tom.board.model.enums.BoardStatus;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.converter.*;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARDLIKE_NOT_FOUND));
        Pin pin = PinConverter.toPinEntity(user, board, pinReq);
        // 빈 파일 필터링. 안하면 파일 없어도 length값 1 됨
        files = (files != null && files.length > 0) ?
                Arrays.stream(files)
                        .filter(file -> !file.isEmpty())
                        .toArray(MultipartFile[]::new) :
                new MultipartFile[0];

        if (!org.springframework.util.ObjectUtils.isEmpty(files)) {
            if (files.length > 3)
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
        if (ObjectUtils.isEmpty(board.getPopularAt()) && BoardConverter.toPinAndCommentCount(board.getPinList()) >= 20) {
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

        Pin pin = pinRepository.findById(pinDto.getId())
                .orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        if (pinDto.getComment() != null) {
            pin.setComment(pinDto.getComment());
        }
        //삭제
        pinDto.getPic()
                .forEach(pic -> amazonS3Util.deleteFile(pic));
        pinPictureRepository.deleteAllByPin(pin);

        log.info("사이즈" + pinDto.getPic().size());

        Pin pinSaved = pinRepository.save(pin);
        log.info("사이즈" + pinSaved.getPinPictureList().size());
        files = (files != null && files.length > 0) ?
                Arrays.stream(files)
                        .filter(file -> !file.isEmpty())
                        .toArray(MultipartFile[]::new) :
                new MultipartFile[0];

//            List<PinPicture> pinPictures = pinPictureRepository.findAllByPinId(pinDto.getId());
        if (!org.springframework.util.ObjectUtils.isEmpty(files)) {
            if (files.length + pinSaved.getPinPictureList().size() > 3) {
                throw new PinHandler(ErrorStatus.PIN_PICTURE_OVERED);
            }
            log.info("사이즈" + pinDto.getPic().size());
            PinPicture newPinPicture = null;
            String fileName = null;
            for (MultipartFile file : files) {
                try {
                    String uuid = UUID.randomUUID().toString();
                    Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
                    fileName = amazonS3Util.upload(file, amazonConfig.getPinPath(), savedUuid);
                } catch (IOException e) {
                    throw new BoardHandler(ErrorStatus.BOARD_FILE_UPLOAD_FAILED);
                }
                newPinPicture = PinConverter.toPinPicture(pin, fileName);
                pinPictureRepository.save(newPinPicture);
                log.info("사이즈" + pinDto.getPic().size());
            }
        }

//            if (!org.springframework.util.ObjectUtils.isEmpty(pin.getPinPictureList())) {
//                //수정으로 삭제된 사진만 남음(중복안된 값)
//                List<String> picUrl;
//                if(org.springframework.util.ObjectUtils.isEmpty(pinDto.getPic())) // request에 pic이 null 일때 null 예외 처리
//                    picUrl = PinConverter.toPicStringIdList(pinPictures);
//                else
//                    picUrl = PinConverter.toPicStringIdList(pinPictures).stream().
//                            filter(o -> pinDto.getPic().stream().noneMatch(Predicate.isEqual(o)))
//                            .toList();
//                for (String pic : picUrl) {
//                    //신고된적 없을시 실제 버킷 사진 데이터 삭제
//                    if (pin.getStatus().equals(PinBoardStatus.ACTIVE) && pin.getReport() == 0)
//                        amazonS3Util.deleteFile(pic);
//
//                    pinPictureRepository.deleteByPic(pic);
//                }
//            }

        return ApiResponse.onSuccess(200);
    }

    //삭제 로직
    //1. 삭제할 때 신고되었으면 삭제 X 상태를 변경하기
    //2. 삭제할 경우 대댓글이 달려있으면 상태를 변경하기
    //3. 대댓글과 신고가 되지 않았을경우 삭제 하기
    //4. cascade임으로 image쪽은 삭제할 필요 없을 뜻?
    public ApiResponse pinDelete(Long pinId) {

        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        //3. 신고 및 대댓글이 없을경우 삭제 하기
        if(pin.getCommentList().isEmpty() && pin.getReport() == 0){
            for(PinPicture pp : pin.pinPictureList){
                amazonS3Util.deleteFile(pp.getPic());
            }
            pinRepository.deleteById(pinId);

            return ApiResponse.onSuccess("삭제에 성공하였습니다.");
        }

        //2. 대댓글이 달려있으면 상태를 변경하기
        String comment = "";
        if(pin.commentList != null && !pin.commentList.isEmpty()){
            pin.setStatus(PinBoardStatus.INACTIVE);
            //서버 용량 줄이기
            for(PinPicture pp : pin.pinPictureList){
                amazonS3Util.deleteFile(pp.getPic());
            }
            comment = "대댓글이 있음으로";
        }
        //1. 삭제할 때 신고되었으면 삭제 X 상태를 변경하기
        if(pin.getReport() > 0){
            pin.setStatus(PinBoardStatus.COMPLAINTDELETE);
            comment = "신고가 있음으로";
        }

        pinRepository.save(pin);
        return ApiResponse.onSuccess( comment + "상태 변경에 성공하였습니다.");

    }

    //댓글 좋아요 추가/제거
    public ApiResponse pinLikeSet(Long pinId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        PinLike likeEntity = pinLikeRepository.findByUserAndPin(user,pin);

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
                pin.setStatus(PinBoardStatus.OVERCOMPLAINT);
            }
            pinRepository.save(pin);

            PinComplaint pinComplaintEntity = PinComplaintConverter.toPinComplaintEntity(reportDto, user, pin);
            pinComplaintRepository.save(pinComplaintEntity);

            PinComplaintPicture pinComplaintPicture;
            for (PinPicture pic : pin.getPinPictureList()) {
                pinComplaintPicture = PinComplaintConverter.toPinComplaintPictureEntity(pic.getPic(),pinComplaintEntity);
                pinComplaintPictureRepository.save(pinComplaintPicture);
            }

            User pinUser = userRepository.findById(pin.getUser().getId()).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
            pinUser.setReport(pinUser.getReport() + 1);
            userRepository.save(pinUser);

            return ApiResponse.onSuccess(200);
        }
    }
}
