package tatbash.translation.yandex.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.YandexCloudTokenProperties;

public class IamTokenKeeperTest {

  private IamTokenKeeper iamTokenKeeper;
  private RestTemplate restTemplate;

  @BeforeEach
  void setup() {
    final var properties = new YandexCloudTokenProperties("token_url", "auth_token", 1);
    this.restTemplate = mock(RestTemplate.class);
    this.iamTokenKeeper = new IamTokenKeeper(this.restTemplate, properties);
  }

  @Test
  void should_not_initialize_token_when_it_did_not_refreshed() {
    assertThat(new IamTokenKeeper(null, null).getIamToken())
        .isNull();
    verify(this.restTemplate, times(0))
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Response.class));
  }

  @Test
  void should_initialize_token_when_it_refreshed() {
    // given:
    when(this.restTemplate.exchange(eq("/tokens"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Response.class)))
        .thenReturn(
            new ResponseEntity<>(
                new Response("iam-token", OffsetDateTime.parse("2000-01-01T00:00:00.000Z")),
                HttpStatus.OK
            )
        );

    // when:
    this.iamTokenKeeper.refreshToken();

    // then:
    assertThat(this.iamTokenKeeper.getIamToken())
        .isEqualTo("iam-token");
    verify(this.restTemplate)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Response.class));
  }

  @Test
  void should_throw_exception_when_response_body_is_null() {
    // given:
    when(this.restTemplate.exchange(eq("/tokens"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Response.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    // then:
    assertThatThrownBy(() -> this.iamTokenKeeper.refreshToken())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("response body can't be null");
    verify(this.restTemplate)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Response.class));
  }
}
