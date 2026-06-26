package coursesellerbot.repository;

import coursesellerbot.entity.Payment;
import coursesellerbot.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(UUID paymentId);

    Optional<Payment> findByTransactionId(String transactionId);

    long countByStatus(PaymentStatus status);

    @org.springframework.data.jpa.repository.Query(
            "select coalesce(sum(p.amount), 0) from Payment p where p.status = :status")
    BigDecimal sumAmountByStatus(PaymentStatus status);
}
