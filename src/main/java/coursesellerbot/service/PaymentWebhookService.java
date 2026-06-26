package coursesellerbot.service;

import coursesellerbot.dto.WebhookData;
import coursesellerbot.entity.Payment;
import coursesellerbot.entity.PaymentStatus;
import coursesellerbot.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

/**
 * Обработка webhook-ов Finik: подтверждение оплаты и запуск выдачи доступа.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookService {

    private final PaymentRepository paymentRepository;
    private final AccessService accessService;

    @Transactional
    public void process(WebhookData webhook) {
        log.info("Webhook Finik: transactionId={}, status={}", webhook.getTransactionId(), webhook.getStatus());

        // Идемпотентность: если такую транзакцию уже обработали — выходим.
        if (webhook.getTransactionId() != null) {
            Optional<Payment> already = paymentRepository.findByTransactionId(webhook.getTransactionId());
            if (already.isPresent() && already.get().getStatus() == PaymentStatus.COMPLETED) {
                log.warn("Webhook уже обработан: transactionId={}", webhook.getTransactionId());
                return;
            }
        }

        String paymentIdStr = webhook.getFields() != null
                ? (String) webhook.getFields().get("paymentId") : null;
        if (paymentIdStr == null) {
            log.error("В webhook нет paymentId, fields={}", webhook.getFields());
            return;
        }

        Payment payment = paymentRepository.findByPaymentId(UUID.fromString(paymentIdStr))
                .orElseThrow(() -> new RuntimeException("Платёж не найден: " + paymentIdStr));

        payment.setTransactionId(webhook.getTransactionId());
        payment.setReceiptNumber(webhook.getReceiptNumber());

        if ("SUCCEEDED".equalsIgnoreCase(webhook.getStatus())) {
            payment.setStatus(PaymentStatus.COMPLETED);
            if (webhook.getTransactionDate() != null) {
                payment.setPaidAt(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(webhook.getTransactionDate()), ZoneId.systemDefault()));
            } else {
                payment.setPaidAt(LocalDateTime.now());
            }
            paymentRepository.save(payment);
            log.info("Оплата подтверждена: paymentId={}", payment.getPaymentId());
            accessService.grantAccess(payment);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.warn("Оплата не прошла: paymentId={}, status={}", payment.getPaymentId(), webhook.getStatus());
        }
    }
}
