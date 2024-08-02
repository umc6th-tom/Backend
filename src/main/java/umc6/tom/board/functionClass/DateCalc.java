package umc6.tom.board.functionClass;

import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateCalc {

    public String boardListDate(LocalDateTime pastDateTime) {
        if(ObjectUtils.isEmpty(pastDateTime))
            return "알 수 없음";

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(pastDateTime, now);
        
        //글 올린 시간과 현재 시각 차이를 계산해서 1분전, 1달전 등 으로 변환
        long years = duration.toDays() / 365;
        if (years > 0) {
            return years + "년 전";
        }

        long months = duration.toDays() / 30;
        if (months > 0) {
            return months + "달 전";
        }

        long weeks = duration.toDays() / 7;
        if (weeks > 0) {
            return weeks + "주 전";
        }

        long days = duration.toDays();
        if (days > 0) {
            return days + "일 전";
        }

        long hours = duration.toHours();
        if (hours > 0) {
            return hours + "시간 전";
        }

        long minutes = duration.toMinutes();
        if (minutes > 0) {
            return minutes + "분 전";
        }

        return "방금 전";
    }

    public String formatDate(LocalDateTime pastDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return pastDateTime.format(formatter);

    }
    public String formatDate2(LocalDateTime pastDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return pastDateTime.format(formatter);

    }
}
