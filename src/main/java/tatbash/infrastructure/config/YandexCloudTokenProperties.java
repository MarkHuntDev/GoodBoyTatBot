package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("yandex.cloud.token")
public record YandexCloudTokenProperties(String url, String authorizationToken, int delayBeforeRefreshInMillis) {

  public YandexCloudTokenProperties {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("url can't be null or empty");
    }
    if (StringUtils.isBlank(authorizationToken)) {
      throw new IllegalArgumentException("authorizationToken can't be null or empty");
    }
  }
}
