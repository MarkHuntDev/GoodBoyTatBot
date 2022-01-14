package tatbash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("yandex.cloud")
public record YandexCloudProperties(String folderId, String authorizationToken) {
}
