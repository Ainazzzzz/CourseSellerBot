package coursesellerbot.repository;

import coursesellerbot.entity.Access;
import coursesellerbot.entity.BotUser;
import coursesellerbot.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {

    List<Access> findAllByUserAndActiveTrue(BotUser user);

    Optional<Access> findFirstByUserAndCourseAndActiveTrue(BotUser user, Course course);

    /** Активные доступы, у которых срок уже истёк — кандидаты на снятие. */
    List<Access> findAllByActiveTrueAndExpiresAtBefore(LocalDateTime moment);

    long countByActiveTrue();

    /** Количество уникальных пользователей с хотя бы одним активным доступом. */
    @Query("SELECT COUNT(DISTINCT a.user) FROM Access a WHERE a.active = true")
    long countDistinctUsersWithActiveAccess();
}
