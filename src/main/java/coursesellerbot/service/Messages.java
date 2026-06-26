package coursesellerbot.service;

import coursesellerbot.entity.Language;

/**
 * Тексты интерфейса бота на русском и кыргызском (с HTML-разметкой Telegram).
 */
public final class Messages {

    private Messages() {
    }

    private static boolean ru(Language lang) {
        return lang == null || lang == Language.RU;
    }

    // ===================== Язык =====================

    public static String chooseLanguage() {
        return "🌍 <b>Тилди тандаңыз / Выберите язык</b>";
    }

    public static String langButtonRu() {
        return "🇷🇺 Русский";
    }

    public static String langButtonKg() {
        return "🇰🇬 Кыргызча";
    }

    public static String changeLangButton(Language lang) {
        return "🌐 Тил / Язык";
    }

    // ===================== Приветствие / меню =====================

    public static String welcome(Language lang) {
        return ru(lang)
                ? "👋 <b>Добро пожаловать!</b>\nРады видеть тебя 💜"
                : "👋 <b>Кош келиңиз!</b>\nСизди көргөнүбүзгө кубанычтабыз 💜";
    }

    /** Заголовок и продающее вступление главного меню. */
    public static String menuHeader(Language lang) {
        return ru(lang)
                ? "✨ <b>Курсы по ИИ и цифровым навыкам</b> ✨\n\n"
                  + "Научись создавать <b>сайты, мультфильмы и видео с помощью нейросетей</b> — "
                  + "с нуля, простым языком, на практике 🚀"
                : "✨ <b>ИИ жана санариптик көндүмдөр курстары</b> ✨\n\n"
                  + "<b>Сайт, мультфильм жана видеону нейросеть менен</b> түзүүнү үйрөн — "
                  + "нөлдөн, жөнөкөй тил менен, практикада 🚀";
    }

    public static String includedLabel(Language lang) {
        return ru(lang) ? "📦 <b>Что входит в доступ:</b>" : "📦 <b>Жеткиликтүүлүккө эмне кирет:</b>";
    }

    /** Преимущества + цена под списком курсов. */
    public static String menuFooter(Language lang, String price, String currency, int months) {
        return ru(lang)
                ? "\n⏳ Доступ на <b>" + months + " мес.</b>\n"
                  + "🔒 Личная ссылка — работает только для тебя\n"
                  + "♾️ Учись в удобном темпе\n\n"
                  + "💰 Полный доступ — <b>" + price + " " + currency + "</b>\n\n"
                  + "Нажми кнопку ниже, чтобы оформить 👇"
                : "\n⏳ <b>" + months + " айга</b> жеткиликтүүлүк\n"
                  + "🔒 Жеке шилтеме — сизге гана иштейт\n"
                  + "♾️ Ыңгайлуу темпте окуңуз\n\n"
                  + "💰 Толук жеткиликтүүлүк — <b>" + price + " " + currency + "</b>\n\n"
                  + "Жазылуу үчүн төмөнкү баскычты басыңыз 👇";
    }

    public static String buyFullButton(Language lang) {
        return ru(lang) ? "💳 Оформить доступ" : "💳 Жеткиликтүүлүк алуу";
    }

    public static String myCoursesButton(Language lang) {
        return ru(lang) ? "👤 Мои курсы" : "👤 Менин курстарым";
    }

    // ===================== Оплата =====================

    public static String paymentCreated(Language lang) {
        return ru(lang)
                ? "🎯 <b>Остался один шаг!</b>\n\n"
                  + "Нажми кнопку ниже и оплати через <b>Finik</b>.\n"
                  + "Сразу после оплаты я пришлю тебе личную ссылку для входа 🔑"
                : "🎯 <b>Бир кадам калды!</b>\n\n"
                  + "Төмөнкү баскычты басып, <b>Finik</b> аркылуу төлөңүз.\n"
                  + "Төлөмдөн кийин дароо жеке кирүү шилтемесин жөнөтөм 🔑";
    }

    public static String payButton(Language lang) {
        return ru(lang) ? "💳 Перейти к оплате" : "💳 Төлөмгө өтүү";
    }

    public static String paymentError(Language lang) {
        return ru(lang)
                ? "⚠️ Не удалось создать платёж. Попробуй ещё раз чуть позже или напиши администратору."
                : "⚠️ Төлөм түзүлгөн жок. Бир аздан кийин кайра аракет кылыңыз же админге жазыңыз.";
    }

    public static String paymentSuccess(Language lang) {
        return ru(lang)
                ? "🎉 <b>Оплата прошла успешно!</b>\n\n"
                  + "Добро пожаловать 💜 Вот твой персональный доступ:"
                : "🎉 <b>Төлөм ийгиликтүү өттү!</b>\n\n"
                  + "Кош келиңиз 💜 Сиздин жеке жеткиликтүүлүгүңүз:";
    }

    public static String joinLinkLabel(Language lang) {
        return ru(lang)
                ? "🔑 <b>Ссылка для входа в канал</b>\n<i>одноразовая, только для тебя — не передавай другим</i>"
                : "🔑 <b>Каналга кирүү шилтемеси</b>\n<i>бир жолку, сизге гана — башкаларга бербеңиз</i>";
    }

    public static String topicsLabel(Language lang) {
        return ru(lang) ? "📂 <b>Разделы курсов:</b>" : "📂 <b>Курстардын бөлүмдөрү:</b>";
    }

    /** Строка курса со ссылкой на нужный раздел (тему). */
    public static String courseTopicLine(Language lang, String courseTitle, String topicLink) {
        if (topicLink != null) {
            return "• <a href=\"" + topicLink + "\">" + courseTitle + "</a>";
        }
        return "• " + courseTitle;
    }

    public static String validUntil(Language lang, String until) {
        return ru(lang)
                ? "\n⏳ Доступ активен до <b>" + until + "</b>"
                : "\n⏳ Жеткиликтүүлүк <b>" + until + "</b> чейин";
    }

    public static String linkFailed(Language lang) {
        return ru(lang)
                ? "(не удалось создать ссылку, напиши администратору)"
                : "(шилтеме түзүлгөн жок, админге жазыңыз)";
    }

    // ===================== Мои курсы =====================

    public static String myCoursesEmpty(Language lang) {
        return ru(lang)
                ? "📭 У тебя пока нет активных курсов.\n\nНажми /start, чтобы оформить доступ 🚀"
                : "📭 Сизде азырынча активдүү курстар жок.\n\nЖазылуу үчүн /start басыңыз 🚀";
    }

    public static String myCoursesTitle(Language lang) {
        return ru(lang) ? "👤 <b>Твои активные курсы:</b>" : "👤 <b>Сиздин активдүү курстарыңыз:</b>";
    }

    public static String myCourseLine(Language lang, String courseTitle, String until) {
        return ru(lang)
                ? "📘 <b>" + courseTitle + "</b>\n   ⏳ до " + until
                : "📘 <b>" + courseTitle + "</b>\n   ⏳ " + until + " чейин";
    }

    // ===================== Истечение / прочее =====================

    public static String expiredNotice(Language lang, String courseTitle) {
        return ru(lang)
                ? "⌛ <b>Срок доступа истёк</b>\n\n"
                  + "Доступ к «" + courseTitle + "» закончился, и ты удалён(а) из канала.\n"
                  + "Хочешь продолжить? Оформи доступ снова через /start 💜"
                : "⌛ <b>Жеткиликтүүлүк мөөнөтү бүттү</b>\n\n"
                  + "«" + courseTitle + "» жеткиликтүүлүгү бүттү, сиз каналдан чыгарылдыңыз.\n"
                  + "Улантасызбы? /start аркылуу кайра жазылыңыз 💜";
    }

    public static String unknownCommand(Language lang) {
        return ru(lang)
                ? "🤔 Не понял команду.\nНажми /start, чтобы открыть меню."
                : "🤔 Команда түшүнүксүз.\nМенюну ачуу үчүн /start басыңыз.";
    }
}
