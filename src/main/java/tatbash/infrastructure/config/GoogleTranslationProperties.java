package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("google.translation")
public record GoogleTranslationProperties(String url) {
  public GoogleTranslationProperties {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("url can't be null or empty");
    }
  }
}
