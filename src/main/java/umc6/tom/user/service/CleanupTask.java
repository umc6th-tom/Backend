package umc6.tom.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CleanupTask {

    private final UserService userService;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleWithDrawUserProcess() {
        userService.deleteUser();
    }
}
