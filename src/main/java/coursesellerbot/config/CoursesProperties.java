package coursesellerbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Описание курсов и пакета из application.yml.
 * Используется сидером {@link coursesellerbot.service.CourseSeeder} для наполнения БД.
 */
@Configuration
@ConfigurationProperties(prefix = "courses")
@Data
public class CoursesProperties {

    private String currency = "сом";
    private int defaultAccessMonths = 6;
    private Bundle bundle = new Bundle();
    private List<Item> items = new ArrayList<>();

    @Data
    public static class Item {
        private String code;
        private BigDecimal price;
        private Integer accessMonths;
        private Long channelId;
        private Long topicId;
        private String titleRu;
        private String titleKg;
        private String descriptionRu;
        private String descriptionKg;
    }

    @Data
    public static class Bundle {
        private boolean enabled = true;
        private String code = "ALL";
        private BigDecimal price;
        private String titleRu;
        private String titleKg;
        private String descriptionRu;
        private String descriptionKg;
    }
}
