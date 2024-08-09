package umc6.tom.gpt.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
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
    public GptReq(String model, String prompt, double temperature, int maxTokens, double topP, int frequencyPenalty, int presencePenalty, User user) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", "Now, your role is that of a tutor. \n" +
                "Here's what you need to do. First, the user will tell you their major in words and ask you a question in words or sentences. \n" +
                "Then you'll have to answer that user ask you to the user. at this time, you must to answer detailed. Then you need to generate example question and correct answer for the user's input, related to the user's major. And the most important thing is to answer in Korean in three parts: answer, example question, and correct answer. The example question and correct answer good to Include funny situations in the example questions so that user can have a little fun while solving them. but should be a little difficult, and require deep thinking! \n" +
                "Every time you generate an example question, I want you to give a different example question."));
        this.messages.add(new Message("user","나의 전공은 " +"의학전공"+ "이야. " + prompt + "에 대해 알려줘 그리고 예시 문제와 정답을 생성해줘."));
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP=topP;
        this.frequencyPenalty=frequencyPenalty;
        this.presencePenalty = presencePenalty;

    }
}