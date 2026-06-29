package coursesellerbot.finik;

import com.fasterxml.jackson.databind.ObjectMapper;
import coursesellerbot.config.FinikConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Создание платежа в Finik. Возвращает URL страницы оплаты (QR Finik).
 * Перенесено и адаптировано из проекта BilimBulak.
 */
@Service
@Slf4j
public class FinikPaymentService {

    private final FinikConfig config;
    private final FinikSignatureUtil signatureService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public FinikPaymentService(FinikConfig config,
                               FinikSignatureUtil signatureService,
                               @Qualifier("finikObjectMapper") ObjectMapper objectMapper,
                               @Qualifier("finikRestTemplate") RestTemplate restTemplate) {
        this.config = config;
        this.signatureService = signatureService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * @param paymentId   наш UUID платежа (вернётся в webhook в fields.paymentId)
     * @param amount      сумма
     * @param description назначение платежа
     * @param redirectUrl куда вернуть пользователя из браузера после оплаты
     * @return URL страницы оплаты Finik
     */
    public String createPayment(UUID paymentId, BigDecimal amount, String description, String redirectUrl)
            throws Exception {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Amount", amount.intValue());
        requestBody.put("CardType", "FINIK_QR");
        requestBody.put("PaymentId", paymentId.toString());
        requestBody.put("RedirectUrl", redirectUrl);

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", config.getAccountId());
        data.put("merchantCategoryCode", config.getMerchantCategoryCode());
        data.put("name_en", truncate(description, 50));
        data.put("description", description);
        data.put("webhookUrl", config.getWebhookUrl());
        requestBody.put("Data", data);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String path = "/v1/payment";
        URI uri = URI.create(config.getBaseUrl() + path);

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", uri.getHost());
        headers.put("x-api-key", config.getApiKey());
        headers.put("x-api-timestamp", timestamp);

        String signature = signatureService.generateSignature(
                "POST", path, headers, null, requestBody, config.getPrivateKeyPath());

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        String url = config.getBaseUrl() + path;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("x-api-key", config.getApiKey());
        httpHeaders.set("x-api-timestamp", timestamp);
        httpHeaders.set("signature", signature);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, httpHeaders);

        log.info("Создаём платёж Finik: paymentId={}, amount={}", paymentId, amount);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("Ответ Finik: status={}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.FOUND && response.getHeaders().getLocation() != null) {
                URI location = response.getHeaders().getLocation();
                String locationStr = location.toString();
                // Успешный ответ Finik — редирект на qr.finik.kg (QR-страница оплаты).
                // failure_redirect_url внутри строки тоже содержит "status=failed" и "/v1/redirect",
                // поэтому проверяем хост основного редиректа, а не весь URL целиком.
                String host = location.getHost() != null ? location.getHost() : "";
                if (!host.contains("finik")) {
                    log.error("Finik отклонил платёж. Redirect: {}. Проверь: 1) публичный ключ зарегистрирован в Finik; "
                            + "2) api-key/account-id боевые; 3) подпись запроса.", locationStr);
                    throw new RuntimeException("Finik отклонил создание платежа: " + locationStr);
                }
                log.info("Получен URL оплаты: {}", locationStr);
                return locationStr;
            }
            log.error("Неожиданный ответ Finik: status={}, body={}", response.getStatusCode(), response.getBody());
            throw new RuntimeException("Неожиданный ответ Finik: "
                    + response.getStatusCode() + ", body: " + response.getBody());

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Ошибка Finik: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Не удалось создать платёж в Finik: " + e.getResponseBodyAsString(), e);
        }
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }
}
