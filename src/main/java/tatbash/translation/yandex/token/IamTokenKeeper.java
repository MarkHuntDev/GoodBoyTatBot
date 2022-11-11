package tatbash.translation.yandex.token;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.YandexCloudTokenProperties;
import tatbash.translation.utils.httpentity.HttpEntityBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class IamTokenKeeper {

  @Qualifier("yandexTokenRestTemplate")
  private final RestTemplate restTemplate;
  private final YandexCloudTokenProperties properties;
  private String iamToken;

  /**
   * Returns always relevant IAM-token.
   *
   * @return iam token as {@link String}
   */
  public String getIamToken() {
    return this.iamToken;
  }

  @Scheduled(fixedDelayString = "${yandex.cloud.token.delayBeforeRefreshInMillis}")
  void refreshToken() {
    final var httpEntity = buildHttpEntity();
    final var response = requestIamToken(httpEntity);
    this.iamToken = extractIamToken(response);
  }

  private HttpEntity<Object> buildHttpEntity() {
    return HttpEntityBuilder
        .builder()
        .withBody(new Request(properties.authorizationToken()))
        .build();
  }

  private ResponseEntity<Response> requestIamToken(HttpEntity<Object> httpEntity) {
    log.info("Request IAM-token");
    return this.restTemplate.exchange("/tokens", HttpMethod.POST, httpEntity, Response.class);
  }

  private String extractIamToken(ResponseEntity<Response> response) {
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NullPointerException("response body can't be null"))
        .iamToken();
  }
}
