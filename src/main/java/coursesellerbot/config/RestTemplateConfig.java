package coursesellerbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate finikRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Jackson 2 ObjectMapper для подписи запросов Finik.
     * Порядок ключей по алфавиту обязателен — иначе подпись не совпадёт.
     */
    @Bean(name = "finikObjectMapper")
    public ObjectMapper finikObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
}
