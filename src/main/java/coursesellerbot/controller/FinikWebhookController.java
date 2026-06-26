package coursesellerbot.controller;

import coursesellerbot.dto.WebhookData;
import coursesellerbot.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Принимает уведомления Finik об оплате.
 * URL должен совпадать с finik.webhook-url в application.yml.
 */
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class FinikWebhookController {

    private final PaymentWebhookService webhookService;

    @PostMapping("/finik")
    public ResponseEntity<Void> handle(@RequestBody WebhookData webhook) {
        try {
            webhookService.process(webhook);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ошибка обработки webhook Finik", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
