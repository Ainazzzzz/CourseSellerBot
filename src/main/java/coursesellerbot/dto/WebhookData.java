package coursesellerbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Тело webhook-а от Finik об изменении статуса платежа.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookData {

    private String id;

    @JsonProperty("transactionId")
    private String transactionId;

    /** SUCCEEDED, FAILED и т.п. */
    private String status;

    private BigDecimal amount;

    private BigDecimal net;

    @JsonProperty("accountId")
    private String accountId;

    /** Доп. поля, среди них наш paymentId. */
    private Map<String, Object> fields;

    @JsonProperty("requestDate")
    private Long requestDate;

    @JsonProperty("transactionDate")
    private Long transactionDate;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("receiptNumber")
    private String receiptNumber;
}
