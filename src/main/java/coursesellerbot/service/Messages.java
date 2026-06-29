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

    // ===================== Приветствие (новый пользователь) =====================

    public static String welcomeScreen() {
        return "👋 <b>Салам! / Привет!</b>\n\n"
                + "Кош келиңиз — бул жерде ИИ жана санариптик көндүмдөр боюнча курстарды таба аласыз!\n\n"
                + "Добро пожаловать — здесь ты найдёшь курсы по ИИ и цифровым навыкам!\n\n"
                + "Нажми кнопку ниже, чтобы начать 👇\n"
                + "Баштоо үчүн төмөнкү баскычты бас 👇";
    }

    public static String startButton() {
        return "🚀 Начать / Баштоо";
    }

    // ===================== Подписи к фото команды =====================

    public static String photoCaptionBektur(Language lang) {
        return ru(lang)
                ? "👨‍💻 <b>Бектур Каныбеков</b>\n"
                  + "Fullstack-разработчик · Стаж 4 года\n"
                  + "🏢 <b>Eltech</b> (при Элдик банке) — 2+ года\n\n"
                  + "🎯 Ведёт: Вайб-кодинг и ИИ-мультики"
                : "👨‍💻 <b>Бектур Каныбеков</b>\n"
                  + "Fullstack-иштеп чыгуучу · Тажрыйба 4 жыл\n"
                  + "🏢 <b>Eltech</b> (Элдик банк астында) — 2+ жыл\n\n"
                  + "🎯 Окутат: Вайб-кодинг жана ИИ-мультфильмдер";
    }

    public static String photoCaptionNursultan(Language lang) {
        return ru(lang)
                ? "🖥️ <b>Нурсултан Кенжебаев</b>\n"
                  + "Frontend-разработчик · Стаж 4 года\n"
                  + "🏢 <b>Mdigital</b> (при Mbank)\n\n"
                  + "💻 Ведёт: Компьютерная грамотность (Windows)"
                : "🖥️ <b>Нурсултан Кенжебаев</b>\n"
                  + "Frontend-иштеп чыгуучу · Тажрыйба 4 жыл\n"
                  + "🏢 <b>Mdigital</b> (Mbank астында)\n\n"
                  + "💻 Окутат: Компьютердик сабаттуулук (Windows)";
    }

    public static String photoCaptionAinazik(Language lang) {
        return ru(lang)
                ? "🍎 <b>Айназик</b>\n"
                  + "Backend-разработчик · Стаж 3+ года\n"
                  + "🏛️ Опыт в гос. секторе: Элдик банк, Налоговая, Таможня\n"
                  + "Сейчас — работает на себя 🚀\n\n"
                  + "💻 Ведёт: Компьютерная грамотность (Mac)"
                : "🍎 <b>Айназик</b>\n"
                  + "Backend-иштеп чыгуучу · Тажрыйба 3+ жыл\n"
                  + "🏛️ Мамл. секторунда: Элдик банк, Салык, Бажы кызматы\n"
                  + "Азыр — өз алдынча иштейт 🚀\n\n"
                  + "💻 Окутат: Компьютердик сабаттуулук (Mac)";
    }

    /** Текстовый блок о команде — показывается когда фото ещё не настроены. */
    public static String teamIntroText(Language lang) {
        return ru(lang)
                ? "👋 <b>Привет! Это мы — ваши преподаватели</b>\n\n"
                  + "👨‍💻 <b>Бектур Каныбеков</b>\n"
                  + "Fullstack-разраб · 4 года стажа · Eltech (Элдик банк), 2+ года\n"
                  + "Ведёт: Вайб-кодинг и ИИ-мультики\n\n"
                  + "🖥️ <b>Нурсултан Кенжебаев</b>\n"
                  + "Frontend-разраб · 4 года стажа · Mdigital (Mbank)\n"
                  + "Ведёт: Компьютерная грамотность (Windows)\n\n"
                  + "🍎 <b>Айназик</b>\n"
                  + "Backend-разраб · 3+ года · Элдик банк, Налоговая, Таможня → сейчас на себе\n"
                  + "Ведёт: Компьютерная грамотность (Mac)\n\n"
                  + "━━━━━━━━━━━━━━━━\n\n"
                : "👋 <b>Салам! Биз — сиздин окутуучуларыңыз</b>\n\n"
                  + "👨‍💻 <b>Бектур Каныбеков</b>\n"
                  + "Fullstack-иштеп чыгуучу · 4 жыл · Eltech (Элдик банк), 2+ жыл\n"
                  + "Окутат: Вайб-кодинг жана ИИ-мультфильмдер\n\n"
                  + "🖥️ <b>Нурсултан Кенжебаев</b>\n"
                  + "Frontend-иштеп чыгуучу · 4 жыл · Mdigital (Mbank)\n"
                  + "Окутат: Компьютердик сабаттуулук (Windows)\n\n"
                  + "🍎 <b>Айназик</b>\n"
                  + "Backend-иштеп чыгуучу · 3+ жыл · Элдик банк, Салык, Бажы → азыр өзү\n"
                  + "Окутат: Компьютердик сабаттуулук (Mac)\n\n"
                  + "━━━━━━━━━━━━━━━━\n\n";
    }

    /** Блок «что умеешь после курсов» + преимущества. Показывается после фото команды ИЛИ после teamIntroText. */
    public static String pitchCourses(Language lang) {
        return ru(lang)
                ? "💡 <b>Что умеешь ПОСЛЕ наших курсов:</b>\n\n"
                  + "🎯 <b>Вайб-кодинг</b>\n"
                  + "Принимаешь заказы, делаешь сайты за пару часов — и зарабатываешь. Без знания кода. Серьёзно 🙌\n\n"
                  + "🎬 <b>ИИ: Мультики и видео</b>\n"
                  + "Запускаешь YouTube или Instagram, делаешь контент с нейросетями и монетизируешь. Идеи есть — теперь есть и инструменты.\n\n"
                  + "💻 <b>Компьютерная грамотность</b>\n"
                  + "Больше не боишься нажать «не ту кнопку». Уверенно работаешь — на Mac или Windows 💪\n\n"
                  + "━━━━━━━━━━━━━━━━\n\n"
                  + "✅ Видеоуроки в своём темпе\n"
                  + "✅ Практика с первого дня\n"
                  + "✅ Личная ссылка — только для тебя\n"
                  + "✅ 6 месяцев без ограничений 🔓\n\n"
                  + "Готов? Смотри курсы и цены 👇"
                : "💡 <b>Курстан кийин эмне кыла аласың:</b>\n\n"
                  + "🎯 <b>Вайб-кодинг</b>\n"
                  + "Заказ кабыл ал, бизнес үчүн сайт жаса, акча тап. Код жазбай-эле. Чын айтабыз 🙌\n\n"
                  + "🎬 <b>ИИ: Мультфильм жана видео</b>\n"
                  + "YouTube же Instagram баштап, нейросеть менен контент жасайсың жана акча табасың. Идеялар бар — куралдар да бар эми.\n\n"
                  + "💻 <b>Компьютердик сабаттуулук</b>\n"
                  + "«Туура эмес баскычты баспаймынбы?» деп корктунбу? Эми жок. Mac же Windows — баары жеңил 💪\n\n"
                  + "━━━━━━━━━━━━━━━━\n\n"
                  + "✅ Видео-сабактар ыңгайлуу темпте\n"
                  + "✅ Биринчи күндөн эле практика\n"
                  + "✅ Жеке шилтеме — сизге гана\n"
                  + "✅ 6 ай чектөөсүз 🔓\n\n"
                  + "Даярсызбы? Курстарды жана баасын кара 👇";
    }

    /** Полный питч-экран без фото (команда + курсы в одном сообщении). */
    public static String pitchText(Language lang) {
        return teamIntroText(lang) + pitchCourses(lang);
    }

    public static String viewCoursesButton(Language lang) {
        return ru(lang) ? "👉 Смотреть курсы и цены" : "👉 Курстарды жана баасын көрүү";
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
                ?
                   "🔒 Личная ссылка — работает только для тебя\n"
                  + "♾️ Учись в удобном темпе\n\n"
                  + "💰 Полный доступ — <b>" + price + " " + currency + "</b>\n\n"
                  + "Нажми кнопку ниже, чтобы оформить 👇"
                :
                  "🔒 Жеке шилтеме — сизге гана иштейт\n"
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

    public static String backButton(Language lang) {
        return ru(lang) ? "⬅️ Назад" : "⬅️ Артка";
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
