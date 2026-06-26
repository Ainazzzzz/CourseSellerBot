package coursesellerbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Регистрирует long-polling бота при старте приложения.
 * Если токен не задан (заглушка) — бот не поднимается, приложение продолжает работать.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BotInitializer {

    private final CourseSellerBot bot;

    @EventListener(ApplicationReadyEvent.class)
    public void registerBot() {
        String token = bot.getBotToken();
        if (token == null || token.isBlank() || token.contains("PUT_YOUR_BOT_TOKEN")) {
            log.warn("BOT_TOKEN не задан — Telegram-бот не запущен. Укажите telegram.bot.token, чтобы включить бота.");
            return;
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            log.info("Telegram-бот запущен: @{}", bot.getBotUsername());
        } catch (Exception e) {
            log.error("Не удалось запустить Telegram-бота", e);
        }
    }
}
