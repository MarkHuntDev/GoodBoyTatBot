package tatbash.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {

  private final YandexCloudProperties properties;

  /**
   * <p>
   * {@link RestTemplate} should be built by {@link RestTemplateBuilder}.
   * See <a href="https://medium.com/@TimvanBaarsen/spring-boot-why-you-should-always-use-the-resttemplatebuilder-to-create-a-resttemplate-instance-d5a44ebad9e9">this article</a> for details.
   * </p>
   */
  @Bean
  RestTemplate yandexTranslationRestTemplate(RestTemplateBuilder builder) {
    return builder
        .rootUri(this.properties.translationUrl())
        .build();
  }
}
