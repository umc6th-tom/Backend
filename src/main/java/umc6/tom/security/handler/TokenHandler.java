package umc6.tom.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import umc6.tom.apiPayload.code.status.ErrorStatus;
import umc6.tom.security.JwtAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class TokenHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[Access Denied] {}", request.getRequestURI());

        JwtAuthenticationFilter.setErrorResponse(response, ErrorStatus.JWT_AUTHORIZATION_FAILED);
    }
}
