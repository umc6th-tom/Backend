package umc6.tom.firebase.converter;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc6.tom.firebase.config.FirebaseConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseConverter { //json 값 보안을 위한 환경변수 값을 json으로 변환
    private final FirebaseConfig firebaseConfig;

    public ByteArrayInputStream getCredentialsFromEnv() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("type", firebaseConfig.getType());
        jsonMap.put("project_id", firebaseConfig.getProject_id());
        jsonMap.put("private_key_id", firebaseConfig.getPrivate_key_id());
        jsonMap.put("private_key", firebaseConfig.getPrivate_key().replace("\\n", "\n"));
        jsonMap.put("client_email", firebaseConfig.getClient_email());
        jsonMap.put("client_id", firebaseConfig.getClient_id());
        jsonMap.put("auth_uri", firebaseConfig.getAuth_uri());
        jsonMap.put("token_uri", firebaseConfig.getToken_uri());
        jsonMap.put("auth_provider_x509_cert_url", firebaseConfig.getAuth_provider_x509_cert_url());
        jsonMap.put("client_x509_cert_url", firebaseConfig.getClient_x509_cert_url());
        jsonMap.put("universe_domain", firebaseConfig.getUniverse_domain());

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonMap);

        try (ByteArrayInputStream stream = new ByteArrayInputStream(jsonString.getBytes())) {
            return stream;
        }
    }
}
