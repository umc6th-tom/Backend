package umc6.tom.firebase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Configuration
public class FirebaseConfig {
    @Value("${firebase.key.type}")
    private String type;
    @Value("${firebase.key.project_id}")
    private String project_id;
    @Value("${firebase.key.private_key_id}")
    private String private_key_id;
    @Value("${firebase.key.private_key}")
    private String private_key;
    @Value("${firebase.key.client_email}")
    private String client_email;
    @Value("${firebase.key.client_id}")
    private String client_id;
    @Value("${firebase.key.auth_uri}")
    private String auth_uri;
    @Value("${firebase.key.token_uri}")
    private String token_uri;
    @Value("${firebase.key.auth_provider_x509_cert_url}")
    private String auth_provider_x509_cert_url;
    @Value("${firebase.key.client_x509_cert_url}")
    private String client_x509_cert_url;
    @Value("${firebase.key.universe_domain}")
    private String universe_domain;


}
