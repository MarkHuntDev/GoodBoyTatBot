package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("telegram.bot.auth")
public record TelegramAuthProperties(String name, String token) {

  /**
   * Params {@code name} and {@code token} can't be null or empty.
   */
  public TelegramAuthProperties {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("name can't be null or empty");
    }
    if (StringUtils.isBlank(token)) {
      throw new IllegalArgumentException("token can't be null or empty");
    }
  }
}
