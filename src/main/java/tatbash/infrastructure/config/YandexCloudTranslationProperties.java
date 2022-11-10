package tatbash.infrastructure.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("yandex.cloud.translation")
public record YandexCloudTranslationProperties(String url, String folderId) {

  public YandexCloudTranslationProperties {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("url can't be null or empty");
    }
    if (StringUtils.isBlank(folderId)) {
      throw new IllegalArgumentException("folderId can't be null or empty");
    }
  }
}
