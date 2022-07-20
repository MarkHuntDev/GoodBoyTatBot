package tatbash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("telegram.bot")
public record TelegramBotProperties(String name, String token) {

  /**
   * Params {@code name} and {@code token} can't be null or empty.
   */
  public TelegramBotProperties {
    if (name == null) {
      throw new NullPointerException("name can't be null");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name can't be empty");
    }
    if (token == null) {
      throw new NullPointerException("token can't be null");
    }
    if (token.isBlank()) {
      throw new IllegalArgumentException("token can't be empty");
    }
  }
}
