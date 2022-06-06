package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("yandex.cloud")
public record YandexCloudProperties(String folderId, String authorizationToken) {
  public YandexCloudProperties {
    if (StringUtils.isAnyBlank(folderId, authorizationToken)) {
      throw new IllegalArgumentException("folderId or authorizationToken can't be null or empty");
    }
  }
}
