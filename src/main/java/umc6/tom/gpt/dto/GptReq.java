package umc6.tom.gpt.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GptReq {

    private String model;
    private List<Message> messages;
    private double temperature;
    private int maxTokens;
    private double topP;
    private int frequencyPenalty;
    private int presencePenalty;
//user.getMajors().getMajor()
    public GptReq(String model, MajorReq.SearchDto searchDto, Majors major, double temperature, int maxTokens, double topP, int frequencyPenalty, int presencePenalty, User user) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", "Now, your role is that of a tutor. \n" +
                "Here's what you need to do. First, the user will tell you their major in words and ask you a question in words or sentences. Then you'll have to answer that user ask you to the user. at this time, you must to answer detailed. Then you need to generate example question and correct answer for the user's input, related to the user's major. And the most important thing is to answer in Korean in three parts: \"답변:\", \"예시문제:\", \"정답:\". The example question and correct answer should be difficult, and require deep thinking! \n" +
                "Every time you generate an answer, I want you to give a different answer."));
        this.messages.add(new Message("user","나의 전공은 " + major.getMajor() + "이야. " + searchDto.getQuestion() + "에 대해 알려줘 그리고 예시 문제와 정답을 생성해줘."));
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP=topP;
        this.frequencyPenalty=frequencyPenalty;
        this.presencePenalty = presencePenalty;

    }
}