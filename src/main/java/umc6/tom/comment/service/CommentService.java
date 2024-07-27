package umc6.tom.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.PinHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
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
import umc6.tom.comment.repository.*;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository pinCommentRepository;
    private final PinPictureRepository pinPictureRepository;
    private final PinConverter pinConverter;
    private final PinLikeRepository pinLikeRepository;
    private final PinComplaintConverter pinComplaintConverter;
    private final PinComplaintRepository pinComplaintRepository;
    private final PinComplaintPictureRepository pinComplaintPictureRepository;

    //댓글 등록
    @Transactional
    public ApiResponse pinRegister(PinReqDto.PinCommentAndPic pinReq, Long boardId, Long userId  ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //임시 에러코드 사용함
        //게시판 댓글 저장
        Board board =  boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BOARDLIKE_NOT_FOUND));
        Pin pin = PinConverter.toPinEntity(user, board, pinReq);
        Pin pinSaved = pinRepository.save(pin);
        //댓글 사진 테이블에 저장하기
        try{
            for(String picUrl : pinReq.getPic()){
                PinPicture pinPicture = PinPictureConverter.toPinPictureEntity(picUrl, pinSaved);
                pinPictureRepository.save(pinPicture);
            }
        }catch(Exception e){
            throw new PinHandler(ErrorStatus.PIN_NOT_REGISTER);
        }
        return ApiResponse.onSuccess(200);
    }

    //댓글 수정하기위해 데이터 주는거!!
    @Transactional
    public ApiResponse getDetailPin(Long commentId) {
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
        PinResDto pinResDto = PinConverter.toPinDto(pin);

        return ApiResponse.onSuccess(pinResDto);
    }

    @Transactional
    public ApiResponse pinModify(PinReqDto.PinAndPic pinDto) {
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

        if(likeEntity == null){
            pinLikeRepository.save(PinLikeConverter.toEntity(user,pin));
            return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_LIKE);
        }else{
            pinLikeRepository.delete(likeEntity);
            return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_UNLIKE);
        }
    }

    //댓글 신고하기
    public ApiResponse pinReport(Long commentId, PinReportReqDto.PinReportDto reportDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));

        PinComplaint pinComplaintEntity = PinComplaintConverter.toPinComplaintEntity(reportDto,user,pin);

        PinComplaint pinComplaintSaved = pinComplaintRepository.save(pinComplaintEntity);

        for(String pic : reportDto.getPic()){
            pinComplaintPictureRepository.save(PinComplaintPicture.builder().pinComplaint(pinComplaintSaved).pic(pic).build());
        }

        return ApiResponse.onSuccess(200);
    }
}
