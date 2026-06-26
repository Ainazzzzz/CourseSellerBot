package coursesellerbot.service;

import coursesellerbot.config.BotProperties;
import coursesellerbot.config.CoursesProperties;
import coursesellerbot.entity.*;
import coursesellerbot.finik.FinikPaymentService;
import coursesellerbot.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Создание платежей за курс или пакет.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FinikPaymentService finikPaymentService;
    private final CoursesProperties coursesProperties;
    private final BotProperties botProperties;

    /** Создаёт платёж за один курс, возвращает URL оплаты Finik. */
    @Transactional
    public String createCoursePayment(BotUser user, Course course) throws Exception {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .user(user)
                .productType(ProductType.COURSE)
                .course(course)
                .amount(course.getPrice())
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        String description = "Оплата курса: " + course.getTitleRu();
        return createInFinik(payment, description);
    }

    /** Создаёт платёж за пакет "все курсы", возвращает URL оплаты Finik. */
    @Transactional
    public String createBundlePayment(BotUser user) throws Exception {
        BigDecimal price = coursesProperties.getBundle().getPrice();
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .user(user)
                .productType(ProductType.BUNDLE)
                .amount(price)
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        String description = "Оплата пакета курсов";
        return createInFinik(payment, description);
    }

    private String createInFinik(Payment payment, String description) throws Exception {
        String redirectUrl = "https://t.me/" + botProperties.getUsername();
        String url = finikPaymentService.createPayment(
                payment.getPaymentId(), payment.getAmount(), description, redirectUrl);
        payment.setPaymentUrl(url);
        paymentRepository.save(payment);
        return url;
    }
}
