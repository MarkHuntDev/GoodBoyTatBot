package tatbash.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "yandex.cloud")
public class YandexCloudProperties {
  private String folderId;
  private String authorizationToken;
}
