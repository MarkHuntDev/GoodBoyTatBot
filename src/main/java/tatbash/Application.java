package tatbash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tatbash.infrastructure.config.TelegramBotProperties;
import tatbash.infrastructure.config.YandexCloudProperties;

@EnableConfigurationProperties({TelegramBotProperties.class, YandexCloudProperties.class})
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
