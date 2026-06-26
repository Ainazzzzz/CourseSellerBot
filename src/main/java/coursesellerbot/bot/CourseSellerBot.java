package coursesellerbot.bot;

import coursesellerbot.config.BotProperties;
import coursesellerbot.config.CoursesProperties;
import coursesellerbot.entity.BotUser;
import coursesellerbot.entity.Course;
import coursesellerbot.entity.Language;
import coursesellerbot.repository.CourseRepository;
import coursesellerbot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Telegram-бот продажи курсов: /start, выбор языка, меню курсов, покупка,
 * мои курсы, статистика для админа.
 */
@Component
@Slf4j
public class CourseSellerBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final CoursesProperties coursesProperties;
    private final CourseRepository courseRepository;
    private final BotUserService userService;
    private final PaymentService paymentService;
    private final AccessService accessService;
    private final StatsService statsService;
    private final TelegramGateway telegram;

    public CourseSellerBot(BotProperties botProperties,
                           CoursesProperties coursesProperties,
                           CourseRepository courseRepository,
                           BotUserService userService,
                           PaymentService paymentService,
                           AccessService accessService,
                           StatsService statsService,
                           TelegramGateway telegram) {
        this.botProperties = botProperties;
        this.coursesProperties = coursesProperties;
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.paymentService = paymentService;
        this.accessService = accessService;
        this.statsService = statsService;
        this.telegram = telegram;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                handleCallback(update);
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                handleText(update);
            }
        } catch (Exception e) {
            log.error("Ошибка обработки апдейта", e);
        }
    }

    // ===================== Текстовые команды =====================

    private void handleText(Update update) {
        Long chatId = update.getMessage().getChatId();
        var from = update.getMessage().getFrom();
        BotUser user = userService.getOrCreate(chatId, from.getUserName(), from.getFirstName());
        String text = update.getMessage().getText().trim();

        if (text.startsWith("/start")) {
            if (user.getLanguage() == null) {
                askLanguage(chatId, null);
            } else {
                showMainMenu(chatId, user.getLanguage(), null);
            }
            return;
        }
        if (text.equals("/menu")) {
            showMainMenu(chatId, langOf(user), null);
            return;
        }
        if (text.equals("/mycourses")) {
            showMyCourses(chatId, user, null);
            return;
        }
        if (text.equals("/lang")) {
            askLanguage(chatId, null);
            return;
        }
        if (text.equals("/stats")) {
            if (isAdmin(from.getId())) {
                telegram.send(chatId, statsService.buildReport());
            } else {
                telegram.send(chatId, Messages.unknownCommand(langOf(user)));
            }
            return;
        }
        telegram.send(chatId, Messages.unknownCommand(langOf(user)));
    }

    // ===================== Callback-кнопки =====================

    private void handleCallback(Update update) {
        var cb = update.getCallbackQuery();
        telegram.answerCallback(cb.getId());

        Long chatId = cb.getMessage().getChatId();
        Integer messageId = cb.getMessage().getMessageId();
        var from = cb.getFrom();
        BotUser user = userService.getOrCreate(chatId, from.getUserName(), from.getFirstName());
        String data = cb.getData();

        if (data.startsWith("lang:")) {
            Language lang = "KG".equals(data.substring(5)) ? Language.KG : Language.RU;
            user = userService.setLanguage(chatId, lang);
            showMainMenu(chatId, lang, messageId);
            return;
        }
        if (data.equals("menu")) {
            showMainMenu(chatId, langOf(user), messageId);
            return;
        }
        if (data.equals("changelang")) {
            askLanguage(chatId, messageId);
            return;
        }
        if (data.equals("mycourses")) {
            showMyCourses(chatId, user, messageId);
            return;
        }
        if (data.equals("buyall")) {
            startBundlePurchase(chatId, user, messageId);
        }
    }

    /** Рисует экран: редактирует текущее сообщение (если есть messageId) или шлёт новое. */
    private void render(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        if (messageId != null) {
            telegram.editText(chatId, messageId, text, keyboard);
        } else {
            telegram.send(chatId, text, keyboard);
        }
    }

    // ===================== Экраны =====================

    private void askLanguage(Long chatId, Integer messageId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(
                button(Messages.langButtonRu(), "lang:RU"),
                button(Messages.langButtonKg(), "lang:KG")
        ));
        render(chatId, messageId, Messages.chooseLanguage(), markup(rows));
    }

    private void showMainMenu(Long chatId, Language lang, Integer messageId) {
        String currency = coursesProperties.getCurrency();
        int months = coursesProperties.getDefaultAccessMonths();

        StringBuilder text = new StringBuilder(Messages.menuHeader(lang)).append("\n\n")
                .append(Messages.includedLabel(lang));
        for (Course course : courseRepository.findAllByActiveTrueOrderBySortOrderAsc()) {
            text.append("\n").append(emojiFor(course.getCode())).append(" ").append(titleOf(course, lang));
        }
        text.append("\n").append(Messages.menuFooter(lang,
                coursesProperties.getBundle().getPrice().toPlainString(), currency, months));

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        String label = Messages.buyFullButton(lang) + " · "
                + coursesProperties.getBundle().getPrice().toPlainString() + " " + currency;
        rows.add(List.of(button(label, "buyall")));
        rows.add(List.of(button(Messages.myCoursesButton(lang), "mycourses")));
        rows.add(List.of(button(Messages.changeLangButton(lang), "changelang")));

        render(chatId, messageId, text.toString(), markup(rows));
    }

    private void startBundlePurchase(Long chatId, BotUser user, Integer messageId) {
        Language lang = langOf(user);
        try {
            String url = paymentService.createBundlePayment(user);
            InlineKeyboardButton payBtn = InlineKeyboardButton.builder()
                    .text(Messages.payButton(lang))
                    .url(url)
                    .build();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            rows.add(List.of(payBtn));
            rows.add(List.of(button(Messages.backButton(lang), "menu")));
            render(chatId, messageId, Messages.paymentCreated(lang), markup(rows));
        } catch (Exception e) {
            log.error("Ошибка создания платежа за пакет", e);
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            rows.add(List.of(button(Messages.backButton(lang), "menu")));
            render(chatId, messageId, Messages.paymentError(lang), markup(rows));
        }
    }

    private void showMyCourses(Long chatId, BotUser user, Integer messageId) {
        Language lang = langOf(user);
        List<String> lines = accessService.activeCoursesForUser(user);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(button(Messages.backButton(lang), "menu")));
        String text = lines.isEmpty()
                ? Messages.myCoursesEmpty(lang)
                : Messages.myCoursesTitle(lang) + "\n\n" + String.join("\n", lines);
        render(chatId, messageId, text, markup(rows));
    }

    // ===================== Утилиты =====================

    private boolean isAdmin(Long userId) {
        return botProperties.adminIds().contains(userId);
    }

    private Language langOf(BotUser user) {
        return user.getLanguage() == null ? Language.RU : user.getLanguage();
    }

    private String titleOf(Course course, Language lang) {
        return lang == Language.KG ? course.getTitleKg() : course.getTitleRu();
    }

    private String emojiFor(String code) {
        return switch (code == null ? "" : code) {
            case "VIBE" -> "🎯";
            case "AICARTOON" -> "🎬";
            case "COMPUTER" -> "💻";
            default -> "📘";
        };
    }

    private String descriptionOf(Course course, Language lang) {
        return lang == Language.KG ? course.getDescriptionKg() : course.getDescriptionRu();
    }

    private InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
    }

    private InlineKeyboardMarkup markup(List<List<InlineKeyboardButton>> rows) {
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }
}
