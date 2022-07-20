package tatbash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("yandex.cloud")
public record YandexCloudProperties(String folderId, String authorizationToken) {

  /**
   * Params {@code folderId} and {@code authorizationToken} can't be null or empty.
   */
  public YandexCloudProperties {
    if (folderId == null) {
      throw new NullPointerException("folderId can't be null");
    }
    if (folderId.isBlank()) {
      throw new IllegalArgumentException("folderId can't be empty");
    }
    if (authorizationToken == null) {
      throw new NullPointerException("authorizationToken can't be null");
    }
    if (authorizationToken.isBlank()) {
      throw new IllegalArgumentException("authorizationToken can't be empty");
    }
  }
}
