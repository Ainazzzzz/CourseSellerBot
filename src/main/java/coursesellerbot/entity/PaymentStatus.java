package coursesellerbot.entity;

/**
 * Статус платежа в Finik.
 */
public enum PaymentStatus {
    PENDING,    // создан, ждём оплату
    COMPLETED,  // оплачен (пришёл webhook SUCCEEDED)
    FAILED      // оплата не прошла
}
