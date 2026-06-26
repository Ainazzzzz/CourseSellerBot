package coursesellerbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Активный доступ пользователя к каналу курса.
 * Создаётся после успешной оплаты, снимается по истечении срока.
 */
@Entity
@Table(name = "accesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private BotUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    /** Персональная одноразовая ссылка-приглашение в канал. */
    @Column(length = 512)
    private String inviteLink;

    @Column(nullable = false)
    private LocalDateTime grantedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime revokedAt;
}
