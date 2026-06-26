package coursesellerbot.service;

import coursesellerbot.entity.*;
import coursesellerbot.repository.AccessRepository;
import coursesellerbot.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Выдача и снятие доступа к каналам курсов.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccessService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final AccessRepository accessRepository;
    private final CourseRepository courseRepository;
    private final TelegramGateway telegram;

    /**
     * Выдаёт доступ по оплаченному платежу. Создаёт по одной одноразовой ссылке
     * на каждый канал (если курсы лежат в одном канале — ссылка одна) и отправляет
     * пользователю ссылку на вход + ссылки на разделы (темы) курсов.
     */
    @Transactional
    public void grantAccess(Payment payment) {
        BotUser user = payment.getUser();
        Language lang = user.getLanguage();

        List<Course> courses = payment.getProductType() == ProductType.BUNDLE
                ? courseRepository.findAllByActiveTrueOrderBySortOrderAsc()
                : List.of(payment.getCourse());

        // Группируем курсы по каналу, сохраняя порядок.
        Map<Long, List<Course>> byChannel = new LinkedHashMap<>();
        for (Course course : courses) {
            if (course != null && course.getChannelId() != null) {
                byChannel.computeIfAbsent(course.getChannelId(), k -> new ArrayList<>()).add(course);
            }
        }

        if (byChannel.isEmpty()) {
            telegram.send(user.getChatId(), Messages.paymentError(lang));
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        StringBuilder body = new StringBuilder(Messages.paymentSuccess(lang)).append("\n");

        for (Map.Entry<Long, List<Course>> entry : byChannel.entrySet()) {
            Long channelId = entry.getKey();
            List<Course> channelCourses = entry.getValue();

            String linkName = "u" + user.getChatId();
            String inviteLink = telegram.createOneTimeInviteLink(channelId, linkName);

            LocalDateTime channelExpiry = now;
            for (Course course : channelCourses) {
                LocalDateTime expiresAt = now.plusMonths(course.getAccessMonths());
                if (expiresAt.isAfter(channelExpiry)) {
                    channelExpiry = expiresAt;
                }
                Access access = Access.builder()
                        .user(user)
                        .course(course)
                        .payment(payment)
                        .inviteLink(inviteLink)
                        .grantedAt(now)
                        .expiresAt(expiresAt)
                        .active(true)
                        .build();
                accessRepository.save(access);
            }

            body.append("\n").append(Messages.joinLinkLabel(lang)).append("\n")
                    .append(inviteLink != null ? inviteLink : Messages.linkFailed(lang)).append("\n");

            body.append("\n").append(Messages.topicsLabel(lang));
            for (Course course : channelCourses) {
                body.append("\n").append(Messages.courseTopicLine(lang, title(course, lang), topicLink(course)));
            }
            body.append("\n").append(Messages.validUntil(lang, channelExpiry.format(DATE_FMT)));
        }

        telegram.send(user.getChatId(), body.toString());
    }

    /**
     * Снимает доступы, у которых истёк срок. Из канала удаляет только если у
     * пользователя не осталось другого активного (непросроченного) доступа к
     * этому же каналу.
     * Запускается планировщиком.
     */
    @Transactional
    public int revokeExpired() {
        LocalDateTime now = LocalDateTime.now();
        List<Access> expired = accessRepository.findAllByActiveTrueAndExpiresAtBefore(now);
        for (Access access : expired) {
            access.setActive(false);
            access.setRevokedAt(now);
            accessRepository.save(access);
        }
        // После снятия флагов решаем, кого реально удалять из каналов.
        for (Access access : expired) {
            BotUser user = access.getUser();
            Course course = access.getCourse();
            Long channelId = course.getChannelId();

            boolean stillHasChannel = accessRepository.findAllByUserAndActiveTrue(user).stream()
                    .anyMatch(a -> channelId != null
                            && channelId.equals(a.getCourse().getChannelId())
                            && a.getExpiresAt().isAfter(now));

            if (!stillHasChannel) {
                telegram.removeFromChannel(channelId, user.getChatId());
                telegram.send(user.getChatId(),
                        Messages.expiredNotice(user.getLanguage(), title(course, user.getLanguage())));
            }
        }
        if (!expired.isEmpty()) {
            log.info("Снято истёкших доступов: {}", expired.size());
        }
        return expired.size();
    }

    /** Прямая ссылка на тему (раздел) внутри форум-канала. */
    private String topicLink(Course course) {
        if (course.getTopicId() == null || course.getChannelId() == null) {
            return null;
        }
        String cid = String.valueOf(course.getChannelId());
        if (cid.startsWith("-100")) {
            cid = cid.substring(4);
        } else if (cid.startsWith("-")) {
            cid = cid.substring(1);
        }
        return "https://t.me/c/" + cid + "/" + course.getTopicId();
    }

    @Transactional(readOnly = true)
    public List<String> activeCoursesForUser(BotUser user) {
        List<String> lines = new ArrayList<>();
        for (Access access : accessRepository.findAllByUserAndActiveTrue(user)) {
            lines.add(Messages.myCourseLine(user.getLanguage(),
                    title(access.getCourse(), user.getLanguage()),
                    access.getExpiresAt().format(DATE_FMT)));
        }
        return lines;
    }

    private String title(Course course, Language lang) {
        return lang == Language.KG ? course.getTitleKg() : course.getTitleRu();
    }
}
