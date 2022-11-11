package tatbash.translation.yandex.translate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.ApplicationConfig;
import tatbash.infrastructure.smoketest.RestClientSmokeTest;
import tatbash.translation.yandex.token.IamTokenKeeper;

@ExtendWith(SpringExtension.class)
@RestClientSmokeTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration"
    }
)
class YandexTranslateClientSmokeTest {

  @MockBean
  private IamTokenKeeper tokenKeeper;

  @Autowired
  private YandexTranslateClient yandexTranslateClient;

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @Test
  void should_successfully_translate() {
    // given:
    final var expectedJsonRequest = """
        {
          "sourceLanguageCode": "ru",
          "targetLanguageCode": "tt",
          "texts": [
            "привет",
            "мир"
          ],
          "folderId": "test-folder-id"
        }
        """;

    final var expectedJsonResponse = """
        {
          "translations": [
            {
              "text": "сәлам"
            },
            {
              "text": "дөнья"
            }
          ]
        }
        """;

    this.mockRestServiceServer
        .expect(requestTo("/translate"))
        .andExpect(header("Authorization", "Bearer test-iam-token"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJsonRequest))
        .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

    when(this.tokenKeeper.getIamToken())
        .thenReturn("test-iam-token");

    // when:
    final var translations =
        this.yandexTranslateClient.translate("ru", "tt", new String[] {"привет", "мир"});

    // then:
    assertThat(translations)
        .containsExactly("сәлам", "дөнья");
  }

  @BeforeEach
  void setUp() {
    this.mockRestServiceServer.reset();
  }

  @AfterEach
  void tearDown() {
    this.mockRestServiceServer.verify();
  }

  @TestConfiguration
  @Import({ApplicationConfig.class, YandexTranslateClient.class})
  static class TestConfig {

    @Autowired
    @Qualifier("yandexTranslationRestTemplate")
    private RestTemplate restTemplate;

    @Bean
    MockRestServiceServer mockRestServiceServer() {
      final var customizer = new MockServerRestTemplateCustomizer();
      customizer.customize(this.restTemplate);
      return customizer.getServer();
    }
  }
}
