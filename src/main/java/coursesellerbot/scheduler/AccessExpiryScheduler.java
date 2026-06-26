package coursesellerbot.scheduler;

import coursesellerbot.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Раз в час проверяет истёкшие доступы и удаляет таких пользователей из каналов.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessExpiryScheduler {

    private final AccessService accessService;

    @Scheduled(fixedDelayString = "${access.expiry-check-ms:3600000}", initialDelay = 60000)
    public void checkExpired() {
        accessService.revokeExpired();
    }
}
