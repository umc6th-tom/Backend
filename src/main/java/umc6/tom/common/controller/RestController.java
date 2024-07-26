package umc6.tom.common.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/health")
    public String healthCheck() {
        return "I'am healthy";
    }
}
