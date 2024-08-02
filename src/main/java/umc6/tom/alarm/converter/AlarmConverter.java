package umc6.tom.alarm.converter;

import umc6.tom.alarm.model.Alarm;
import umc6.tom.alarm.model.enums.Field;
import umc6.tom.board.model.Board;

public class AlarmConverter {

    public static Alarm toAlarm(Board board, Field field, String alarm){
        return Alarm.builder()
                .field(field)
                .user(board.getUser())
                .board(board)
                .alarm(alarm)
                .build();
    }
}
