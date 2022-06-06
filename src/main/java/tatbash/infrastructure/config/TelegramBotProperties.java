package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("telegram.bot")
public record TelegramBotProperties(String name, String token) {
  public TelegramBotProperties {
    if (StringUtils.isAnyBlank(name, token)) {
      throw new IllegalArgumentException("name or token can not be null or empty");
    }
  }
}
