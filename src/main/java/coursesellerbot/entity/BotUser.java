package coursesellerbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Пользователь Telegram, общающийся с ботом.
 */
@Entity
@Table(name = "bot_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Telegram ID пользователя (он же chatId в личке с ботом). */
    @Column(nullable = false, unique = true)
    private Long chatId;

    private String username;

    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    private Language language;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
