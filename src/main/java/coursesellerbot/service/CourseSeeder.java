package coursesellerbot.service;

import coursesellerbot.config.CoursesProperties;
import coursesellerbot.entity.Course;
import coursesellerbot.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * При старте приложения создаёт/обновляет курсы в БД на основе application.yml.
 * Цены, тексты и channel-id берутся из конфига — менять их можно там же.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CourseSeeder implements CommandLineRunner {

    private final CoursesProperties properties;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<CoursesProperties.Item> items = properties.getItems();
        int order = 0;
        for (CoursesProperties.Item item : items) {
            order++;
            Course course = courseRepository.findByCode(item.getCode())
                    .orElseGet(Course::new);
            course.setCode(item.getCode());
            course.setPrice(item.getPrice());
            course.setChannelId(item.getChannelId());
            course.setTopicId(item.getTopicId());
            course.setAccessMonths(item.getAccessMonths() != null
                    ? item.getAccessMonths() : properties.getDefaultAccessMonths());
            course.setTitleRu(item.getTitleRu());
            course.setTitleKg(item.getTitleKg());
            course.setDescriptionRu(item.getDescriptionRu());
            course.setDescriptionKg(item.getDescriptionKg());
            course.setActive(true);
            course.setSortOrder(order);
            courseRepository.save(course);
        }
        log.info("Курсы синхронизированы из конфига: {} шт.", items.size());
    }
}
