package umc6.tom.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    @Value("${coolSms.api.sender}")
    private String sender;
    @Value("${coolSms.api.key}")
    private String apiKey;
    @Value("${coolSms.api.secretKey}")
    private String apiSecret;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr/");
    }

    public SingleMessageSentResponse sendMessage(String to, String verificationCode) {
        Message message = new Message();

        message.setFrom(sender);
        message.setTo(to);
        message.setText("[Yesol] 아래의 인증번호를 입력해주세요\n\n인증번호 : " + verificationCode);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
