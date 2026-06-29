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
import java.util.List;

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
        long activeUsers = accessRepository.countDistinctUsersWithActiveAccess();
        List<coursesellerbot.entity.BotUser> allUsers = userRepository.findAll(
                org.springframework.data.domain.Sort.by("createdAt").ascending());
        BigDecimal revenue = paymentRepository.sumAmountByStatus(PaymentStatus.COMPLETED);
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }
        String currency = coursesProperties.getCurrency();

        StringBuilder sb = new StringBuilder();
        sb.append("📊 Статистика\n\n")
          .append("👥 Пользователей бота: ").append(allUsers.size()).append("\n")
          .append("✅ Успешных оплат: ").append(purchases).append("\n")
          .append("🔓 Активных пользователей: ").append(activeUsers).append("\n")
          .append("💰 Выручка: ").append(revenue.toPlainString()).append(" ").append(currency)
          .append("\n\n👤 Список пользователей:\n");

        for (coursesellerbot.entity.BotUser u : allUsers) {
            String name = u.getUsername() != null ? "@" + u.getUsername() : u.getFirstName();
            sb.append("• ").append(name).append("\n");
        }

        return sb.toString().trim();
    }
}
