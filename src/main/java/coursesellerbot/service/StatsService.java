package coursesellerbot.service;

import coursesellerbot.config.CoursesProperties;
import coursesellerbot.entity.PaymentStatus;
import coursesellerbot.repository.AccessRepository;
import coursesellerbot.repository.BotUserRepository;
import coursesellerbot.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Статистика для администратора (/stats).
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final PaymentRepository paymentRepository;
    private final AccessRepository accessRepository;
    private final BotUserRepository userRepository;
    private final CoursesProperties coursesProperties;

    @Transactional(readOnly = true)
    public String buildReport() {
        long purchases = paymentRepository.countByStatus(PaymentStatus.COMPLETED);
        long activeAccesses = accessRepository.countByActiveTrue();
        long users = userRepository.count();
        BigDecimal revenue = paymentRepository.sumAmountByStatus(PaymentStatus.COMPLETED);
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }
        String currency = coursesProperties.getCurrency();

        return "📊 Статистика\n\n"
                + "👥 Пользователей бота: " + users + "\n"
                + "✅ Успешных оплат: " + purchases + "\n"
                + "🔓 Активных доступов: " + activeAccesses + "\n"
                + "💰 Выручка: " + revenue.toPlainString() + " " + currency;
    }
}
