package umc6.tom.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc6.tom.apiPayload.ApiResponse;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.apiPayload.exception.handler.BoardHandler;
import umc6.tom.apiPayload.exception.handler.PinHandler;
import umc6.tom.apiPayload.exception.handler.UserHandler;
import umc6.tom.board.model.Board;
import umc6.tom.board.repository.BoardRepository;
import umc6.tom.comment.converter.PinConverter;
import umc6.tom.comment.converter.PinPictureConverter;
import umc6.tom.comment.dto.PinDto;
import umc6.tom.comment.dto.PinReqDto;
import umc6.tom.comment.dto.PinResDto;
import umc6.tom.comment.model.Pin;
import umc6.tom.comment.model.PinPicture;
import umc6.tom.comment.repository.PinCommentRepository;
import umc6.tom.comment.repository.PinPictureRepository;
import umc6.tom.comment.repository.PinRepository;
import umc6.tom.user.model.User;
import umc6.tom.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final PinCommentRepository pinCommentRepository;
    private final PinPictureRepository pinPictureRepository;

    //댓글 등록
    @Transactional
    public ApiResponse pinRegister(PinReqDto pinReq, Long userId, Long boardId ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //임시 에러코드 사용함
        //게시판 댓글 저장
        Board board =  boardRepository.findById(boardId).orElseThrow(() -> new BoardHandler(ErrorStatus.BoardLike_NOT_FOUND));
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

    //댓글 수정하기위해 줌
    @Transactional
    public ApiResponse getDetailPin(Long commentId) {
        Pin pin = pinRepository.findById(commentId).orElseThrow(() -> new PinHandler(ErrorStatus.PIN_NOT_FOUND));
        PinResDto pinResDto = PinConverter.toPinDto(pin);

        return ApiResponse.onSuccess(pinResDto);
    }
}
