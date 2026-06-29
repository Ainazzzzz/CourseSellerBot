package coursesellerbot.service;

import coursesellerbot.bot.CourseSellerBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обёртка над ботом для отправки сообщений и администрирования каналов.
 * Бот внедряется лениво, чтобы не было циклической зависимости (бот -> сервисы -> шлюз -> бот).
 */
@Service
@Slf4j
public class TelegramGateway {

    private final ObjectProvider<CourseSellerBot> botProvider;

    public TelegramGateway(ObjectProvider<CourseSellerBot> botProvider) {
        this.botProvider = botProvider;
    }

    /** Лениво получаем реального бота (без CGLIB-прокси), разрывая цикл зависимостей. */
    private CourseSellerBot bot() {
        return botProvider.getObject();
    }

    public void send(Long chatId, String text) {
        send(chatId, text, null);
    }

    public void send(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("HTML")
                .disableWebPagePreview(true)
                .replyMarkup(keyboard)
                .build();
        try {
            bot().execute(message);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение chatId={}", chatId, e);
        }
    }

    /** Отправляет фото по file_id с HTML-подписью. */
    public void sendPhoto(Long chatId, String fileId, String caption) {
        SendPhoto photo = SendPhoto.builder()
                .chatId(chatId.toString())
                .photo(new InputFile(fileId))
                .caption(caption)
                .parseMode("HTML")
                .build();
        try {
            bot().execute(photo);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить фото chatId={} fileId={}", chatId, fileId, e);
        }
    }

    public void editText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText edit = EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .parseMode("HTML")
                .disableWebPagePreview(true)
                .replyMarkup(keyboard)
                .build();
        try {
            bot().execute(edit);
        } catch (TelegramApiException e) {
            log.error("Не удалось отредактировать сообщение chatId={}", chatId, e);
        }
    }

    public void answerCallback(String callbackId) {
        try {
            bot().execute(AnswerCallbackQuery.builder().callbackQueryId(callbackId).build());
        } catch (TelegramApiException e) {
            log.error("Не удалось ответить на callback {}", callbackId, e);
        }
    }

    /**
     * Создаёт персональную одноразовую ссылку-приглашение (member_limit = 1),
     * поэтому ею сможет воспользоваться только один человек.
     *
     * @return ссылку-приглашение или null, если не удалось.
     */
    public String createOneTimeInviteLink(Long channelId, String name) {
        if (channelId == null || channelId == 0L) {
            log.error("Не задан channelId для курса — ссылку создать нельзя");
            return null;
        }
        CreateChatInviteLink request = CreateChatInviteLink.builder()
                .chatId(channelId.toString())
                .name(name == null ? "" : (name.length() > 32 ? name.substring(0, 32) : name))
                .memberLimit(1)
                .build();
        try {
            ChatInviteLink link = bot().execute(request);
            return link.getInviteLink();
        } catch (TelegramApiException e) {
            log.error("Не удалось создать ссылку-приглашение для channelId={}", channelId, e);
            return null;
        }
    }

    /**
     * Удаляет пользователя из канала (ban + unban), чтобы при будущей покупке
     * он смог зайти снова по новой ссылке.
     */
    public void removeFromChannel(Long channelId, Long userId) {
        if (channelId == null || channelId == 0L) {
            return;
        }
        try {
            bot().execute(BanChatMember.builder()
                    .chatId(channelId.toString())
                    .userId(userId)
                    .build());
            bot().execute(UnbanChatMember.builder()
                    .chatId(channelId.toString())
                    .userId(userId)
                    .onlyIfBanned(true)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Не удалось удалить userId={} из channelId={}", userId, channelId, e);
        }
    }
}
