package tatbash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableConfigurationProperties
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init(); // https://github.com/rubenlagus/TelegramBots/wiki/FAQ#spring_boot_starter
        SpringApplication.run(Application.class, args);
    }
}
