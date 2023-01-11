package tatbash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.infrastructure.config.GoogleTranslationProperties;
import tatbash.infrastructure.config.TelegramAuthProperties;
import tatbash.infrastructure.config.YandexCloudTokenProperties;
import tatbash.infrastructure.config.YandexCloudTranslationProperties;

@EnableConfigurationProperties({
    ApplicationProperties.class,
    TelegramAuthProperties.class,
    YandexCloudTranslationProperties.class,
    YandexCloudTokenProperties.class,
    GoogleTranslationProperties.class
})
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
