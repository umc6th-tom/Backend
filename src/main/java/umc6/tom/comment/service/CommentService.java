package umc6.tom.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.code.status.SuccessStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.CommentHandler;
import umc6.tom.apiPayload.exception.handler.PinHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.model.Board;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.converter.*;
import umc6.tom.comment.dto.CommentResDto;
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
    private final CommentRepository commentRepository;
    private final CommentPictureRepository commentPictureRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentComplaintRepository commentComplaintRepository;
    private final CommentComplaintPictureRepository commentComplaintPictureRepository;

    //댓글 등록
    @Transactional
    public ApiResponse commentRegister(PinReqDto.PinCommentAndPic pinReq, Long pinId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //임시 에러코드 사용함
        //게시판 댓글 저장
        Pin pin =  pinRepository.findById(pinId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
        Comment comment = CommentConverter.toCommentEntity(user, pin, pinReq);
        Comment commentSaved = commentRepository.save(comment);
        //댓글 사진 테이블에 저장하기
        try{
            for(String picUrl : pinReq.getPic()){
                CommentPicture commentPicture = CommentPictureConverter.toCommentPictureEntity(picUrl, commentSaved);
                commentPictureRepository.save(commentPicture);
            }
        }catch(Exception e){
            throw new PinHandler(ErrorStatus.PIN_NOT_REGISTER);
        }
        return ApiResponse.onSuccess(200);
    }

    //댓글 수정하기위해 데이터 주는거!!
    @Transactional
    public ApiResponse getDetailComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        CommentResDto CommentResDto = CommentConverter.toCommentDto(comment);

        return ApiResponse.onSuccess(CommentResDto);
    }

    @Transactional
    public ApiResponse commentModify(PinReqDto.PinAndPic commentDto) {
        try {
            Comment existingComment = commentRepository.findById(commentDto.getId())
                    .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

            if (commentDto.getComment() != null) {
                existingComment.setComment(commentDto.getComment());
            }
            Comment commentSaved = commentRepository.save(existingComment);
            commentPictureRepository.deleteAllByComment(commentSaved);

            for (String picUrl : commentDto.getPic()) {
                CommentPicture commentPictureEntity = CommentPictureConverter.toCommentPictureEntity(picUrl, commentSaved);
                commentPictureRepository.save(commentPictureEntity);
            }

            return ApiResponse.onSuccess(200);
        }
        catch(Exception e){
            throw new CommentHandler(ErrorStatus.COMMENT_NOT_UPDATE);
        }
    }

    public ApiResponse commentDelete(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new CommentHandler(ErrorStatus.COMMENT_NOT_DELETE);
        }
        return ApiResponse.onSuccess(200);
    }

    //댓글 좋아요 추가/제거
    public ApiResponse commentLikeSet(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        CommentLike likeEntity = commentLikeRepository.findByUser(user);

        if(likeEntity == null){
            commentLikeRepository.save(CommentLikeConverter.toEntity(user,comment));
            return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_LIKE);
        }else{
            commentLikeRepository.delete(likeEntity);
            return ApiResponse.onSuccessWithoutResult(SuccessStatus.PIN_UNLIKE);
        }
    }

    //대댓글 신고하기
    public ApiResponse pinReport(Long commentId, PinReportReqDto.PinReportDto reportDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        CommentComplaint commentComplaintEntity = CommentComplaintConverter.toCommentComplaintEntity(reportDto,user,comment);

        CommentComplaint commentComplaintSaved = commentComplaintRepository.save(commentComplaintEntity);

        for(String pic : reportDto.getPic()){
            commentComplaintPictureRepository.save(CommentComplaintPicture.builder().commentComplaint(commentComplaintSaved).pic(pic).build());
        }

        return ApiResponse.onSuccess(200);
    }
}
