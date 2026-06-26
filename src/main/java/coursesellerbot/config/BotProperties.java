package coursesellerbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Настройки Telegram-бота из application.yml (telegram.bot.*).
 */
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
@Data
public class BotProperties {

    private String username;
    private String token;
    /** Список Telegram ID администраторов через запятую. */
    private String admins = "";

    public Set<Long> adminIds() {
        Set<Long> ids = new HashSet<>();
        if (admins == null || admins.isBlank()) {
            return ids;
        }
        for (String part : admins.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                try {
                    ids.add(Long.parseLong(trimmed));
                } catch (NumberFormatException ignored) {
                    // пропускаем некорректные значения
                }
            }
        }
        return ids;
    }
}
