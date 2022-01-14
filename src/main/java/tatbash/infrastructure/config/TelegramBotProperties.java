package tatbash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("telegram.bot")
public record TelegramBotProperties(String name, String token) {
}
