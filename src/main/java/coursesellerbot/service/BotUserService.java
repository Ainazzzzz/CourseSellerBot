package coursesellerbot.service;

import coursesellerbot.entity.BotUser;
import coursesellerbot.entity.Language;
import coursesellerbot.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BotUserService {

    private final BotUserRepository userRepository;

    @Transactional
    public BotUser getOrCreate(Long chatId, String username, String firstName) {
        return userRepository.findByChatId(chatId).orElseGet(() -> {
            BotUser user = BotUser.builder()
                    .chatId(chatId)
                    .username(username)
                    .firstName(firstName)
                    .build();
            return userRepository.save(user);
        });
    }

    @Transactional
    public BotUser setLanguage(Long chatId, Language language) {
        BotUser user = userRepository.findByChatId(chatId)
                .orElseGet(() -> userRepository.save(BotUser.builder().chatId(chatId).build()));
        user.setLanguage(language);
        return userRepository.save(user);
    }
}
