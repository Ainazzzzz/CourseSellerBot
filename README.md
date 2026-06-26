# CourseSellerBot

Telegram-бот для продажи доступа к курсам в закрытых каналах.
Оплата — через **Finik** (код перенесён из проекта BilimBulak), после оплаты бот выдаёт
**персональную одноразовую** ссылку-приглашение в канал. По истечении срока (по умолчанию 6 месяцев)
доступ автоматически снимается — пользователь удаляется из канала.

## Возможности

- Выбор языка при `/start` — русский / кыргызский
- Меню курсов: покупка одного курса **или** пакета «всё сразу»
- Оплата через Finik QR (`/v1/payment`, подпись RSA)
- Webhook `POST /api/webhooks/finik` подтверждает оплату
- Персональная одноразовая ссылка в канал (`createChatInviteLink`, `member_limit = 1`) —
  ею можно воспользоваться только один раз, передать другому нельзя
- `/mycourses` — мои активные курсы и срок действия
- `/stats` — статистика для администратора (кол-во покупок, активные доступы, выручка)
- Планировщик раз в час снимает истёкшие доступы (удаляет из канала + уведомляет)

## Что нужно настроить перед запуском

### 1. Бот в Telegram
1. Создайте бота через [@BotFather](https://t.me/BotFather), получите **токен** и **username**.
2. Создайте по одному **закрытому каналу** на каждый курс.
3. Добавьте бота **администратором** каждого канала с правами:
   - «Пригласительные ссылки» (Invite users via link)
   - «Блокировка пользователей» (Ban users) — нужно для снятия доступа.
4. Узнайте **числовой ID** канала (например `-1001234567890`): перешлите любой пост канала
   боту [@getidsbot](https://t.me/getidsbot) или [@userinfobot](https://t.me/userinfobot).
5. Узнайте свой Telegram ID (тот же `@userinfobot`) — он понадобится для `/stats`.

### 2. Finik
Боевые значения возьмите в кабинете Finik (сейчас в `application.yml` стоят значения из BilimBulak):
- `FINIK_API_KEY`, `FINIK_ACCOUNT_ID`
- приватный ключ — файл `src/main/resources/finik_private.pem` (уже скопирован; **в git не коммитится**)
- `APP_BASE_URL` — публичный адрес приложения, Finik будет слать на него webhook.
  Итоговый URL вебхука: `<APP_BASE_URL>/api/webhooks/finik`. Для локальной разработки можно
  поднять туннель (ngrok/cloudflared) и указать его адрес.

### 3. База данных
Нужен PostgreSQL. Создайте базу `course_seller_bot` (или задайте свою через `DB_URL`).
Таблицы создаются автоматически (`spring.jpa.hibernate.ddl-auto=update`).

### 4. Курсы, цены, каналы
Всё редактируется в `src/main/resources/application.yml`, раздел `courses:`
(коды курсов, названия на двух языках, цены, `channel-id`, срок доступа, цена пакета).
При старте курсы синхронизируются в БД автоматически.

## Запуск

Через переменные окружения (рекомендуется — секреты не попадают в код):

```bash
export BOT_USERNAME=my_course_bot
export BOT_TOKEN=123456:ABC...           # токен от BotFather
export BOT_ADMINS=123456789              # ваш Telegram ID (через запятую, если несколько)

export CHANNEL_VIBE=-1001111111111
export CHANNEL_AICARTOON=-1002222222222
export CHANNEL_COMPUTER=-1003333333333

export DB_URL=jdbc:postgresql://localhost:5432/course_seller_bot
export DB_USERNAME=postgres
export DB_PASSWORD=postgres

export APP_BASE_URL=https://your-public-url     # без /api/webhooks/finik в конце
export FINIK_API_KEY=...
export FINIK_ACCOUNT_ID=...

./mvnw spring-boot:run
```

Если `BOT_TOKEN` не задан, приложение всё равно стартует, но бот не подключается
(удобно для проверки сборки).

## Как это работает

1. `/start` → выбор языка → меню курсов.
2. Пользователь выбирает курс/пакет → бот создаёт платёж в Finik и присылает кнопку «Оплатить».
3. После оплаты Finik шлёт webhook → бот помечает платёж оплаченным и
   создаёт персональные одноразовые ссылки на нужные каналы, отправляет их пользователю.
4. Срок доступа = `access-months` курса (по умолчанию 6 мес.).
5. Планировщик `AccessExpiryScheduler` раз в час снимает истёкшие доступы.

## Структура

```
config/      — настройки (Finik, бот, курсы, RestTemplate)
finik/       — интеграция Finik (подпись RSA, создание платежа)
dto/         — WebhookData
entity/      — BotUser, Course, Payment, Access + enum'ы
repository/  — JPA-репозитории
service/     — бизнес-логика (платежи, доступы, статистика, i18n, шлюз Telegram)
bot/         — сам бот и его регистрация
controller/  — webhook Finik
scheduler/   — снятие истёкших доступов
```
