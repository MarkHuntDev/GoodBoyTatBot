package tatbash.translation.yandex.translate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.YandexCloudTranslationProperties;
import tatbash.translation.yandex.token.IamTokenKeeper;

class YandexTranslateClientTest {

  private YandexTranslateClient translateClient;
  private RestTemplate restTemplate;
  private IamTokenKeeper tokenKeeper;

  @BeforeEach
  void setup() {
    final var properties = new YandexCloudTranslationProperties("translation_url", "folder_id");
    this.restTemplate = mock(RestTemplate.class);
    this.tokenKeeper = mock(IamTokenKeeper.class);
    this.translateClient = new YandexTranslateClient(this.restTemplate, properties, this.tokenKeeper);
  }

  @Test
  void should_successfully_translate() {
    // given:
    when(this.restTemplate.exchange(eq("/translate"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Response.class)))
        .thenReturn(
            new ResponseEntity<>(
                new Response(
                    new Response.TranslationText[] {
                        new Response.TranslationText("сәлам, дөнья")
                    }
                ),
                HttpStatus.OK
            )
        );
    when(this.tokenKeeper.getIamToken())
        .thenReturn("iam-token");
    // when:
    final var translate = translateClient
        .translate("ru", "tt", "привет, мир");
    // then:
    assertThat(translate)
        .isEqualTo("сәлам, дөнья");
    verify(this.restTemplate)
        .exchange(eq("/translate"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Response.class));
  }

  @Test
  void should_throw_exception_when_response_body_is_null() {
    // given:
    when(this.restTemplate.exchange(eq("/translate"), eq(HttpMethod.POST), any(HttpEntity.class), eq(Response.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
    // then:
    assertThatThrownBy(() -> this.translateClient.translate("", "", ""))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("response body can't be null");
    verify(this.restTemplate)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Response.class));
  }
}
