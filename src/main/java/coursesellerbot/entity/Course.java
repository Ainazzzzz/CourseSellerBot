package coursesellerbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Курс. Каждому курсу соответствует свой закрытый Telegram-канал,
 * куда бот выдаёт персональные одноразовые ссылки-приглашения.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Короткий код курса из конфига (VIBE, AICARTOON, COMPUTER...). */
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @Column(nullable = false)
    private String titleRu;

    @Column(nullable = false)
    private String titleKg;

    @Column(length = 1000)
    private String descriptionRu;

    @Column(length = 1000)
    private String descriptionKg;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Числовой ID закрытого канала (например -1001234567890). */
    private Long channelId;

    /** ID темы (topic) внутри форум-канала, если курс — это раздел одного канала. Может быть null. */
    private Long topicId;

    /** Срок доступа в месяцах. */
    @Column(nullable = false)
    private Integer accessMonths;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Integer sortOrder;
}
