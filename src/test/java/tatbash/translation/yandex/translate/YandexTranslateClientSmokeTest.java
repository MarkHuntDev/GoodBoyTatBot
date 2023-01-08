package tatbash.translation.yandex.translate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.ApplicationConfig;
import tatbash.infrastructure.exception.YandexTranslateException;
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
            "привет, мир"
          ],
          "folderId": "test-folder-id"
        }
        """;

    final var expectedJsonResponse = """
        {
          "translations": [
            {
              "text": "сәлам, дөнья"
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
    final var translation =
        this.yandexTranslateClient.translate("ru", "tt", "привет, мир");

    // then:
    assertThat(translation)
        .isEqualTo("сәлам, дөнья");
  }

  @ParameterizedTest
  @MethodSource("errorRequestsCandidates")
  void should_throw_exception_when_unsuccessful_request(DefaultResponseCreator response, String message) {
    // given:
    this.mockRestServiceServer
        .expect(requestTo("/translate"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andRespond(response);
    // when:
    assertThatThrownBy(() -> yandexTranslateClient.translate("xx", "yy", "foo bar"))
        .isInstanceOf(YandexTranslateException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> errorRequestsCandidates() {
    return Stream.of(
        Arguments.of(
            withBadRequest().body(
                """
                    {
                       "code": 1,
                       "message": "Ошибка bad request",
                       "details": [
                         {
                           "@type": "type.googleapis.com/google.rpc.RequestInfo",
                           "requestId": "00000000-0000-0000-0000-000000000001"
                         }
                       ]
                     }
                    """
            ),
            "Ошибка bad request"
        ),
        Arguments.of(
            withServerError().body(
                """
                    {
                       "code": 1,
                       "message": "Ошибка internal server error",
                       "details": [
                         {
                           "@type": "type.googleapis.com/google.rpc.RequestInfo",
                           "requestId": "00000000-0000-0000-0000-000000000001"
                         }
                       ]
                     }
                    """
            ),
            "Ошибка internal server error"
        )
    );
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
