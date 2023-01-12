package tatbash.translation.google;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static tatbash.infrastructure.utils.TestUtils.readString;
import static tatbash.translation.utils.Utils.quote;

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
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.ApplicationConfig;
import tatbash.infrastructure.exception.GoogleTranslateException;
import tatbash.infrastructure.integrationtest.RestClientIntegrationTest;

@ExtendWith(SpringExtension.class)
@RestClientIntegrationTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration"
    }
)
class GoogleTranslateClientIntegrationTest {

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @Autowired
  private GoogleTranslateClient translateClient;

  @Test
  void should_translate_successfully() {
    // given:
    final var expectedJsonResponse = readString("google_translate_real_successful_response.json");
    mockRestServiceServer
        .expect(method(HttpMethod.POST))
        .andExpect(content().formData(params("ru", "tt", "\"Привет, мир! Я учу татарский язык.\"")))
        .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));
    // when:
    final var translation =
        translateClient.translate("ru", "tt", "Привет, мир! Я учу татарский язык.");
    // then:
    assertThat(translation)
        .isEqualTo("Исәнмесез дөнья! Мин татар телен өйрәнәм.");
  }

  @ParameterizedTest
  @MethodSource("incorrectJsonResponseCandidates")
  void should_throw_exception_when_response_is_incorrect(String json,
                                                         String sourceLang,
                                                         String targetLang,
                                                         String text,
                                                         String errorMessage) {
    // given:
    mockRestServiceServer
        .expect(method(HttpMethod.POST))
        .andExpect(content().formData(params(sourceLang, targetLang, quote(text))))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
    // then:
    assertThatThrownBy(() -> translateClient.translate(sourceLang, targetLang, text))
        .isInstanceOf(GoogleTranslateException.class)
        .hasMessage(errorMessage);
  }

  private static Stream<Arguments> incorrectJsonResponseCandidates() {
    return Stream.of(
        Arguments.of("{}", "ru", "tt", "Text example 1", "rootNode is not an array"),
        Arguments.of("[{}]", "tt", "ru", "Text example 2", "firstNode is not an array"),
        Arguments.of("[[{}]]", "ru", "tt", "Text example 3", "secondNode is not an array")
    );
  }

  @Test
  void should_throw_exception_when_unsuccessful_request() {
    // given:
    mockRestServiceServer
        .expect(method(HttpMethod.POST))
        .andExpect(content().formData(params("xx", "yy", "\"foo bar\"")))
        .andRespond(withServerError().body("unknown error occurred"));
    // when:
    assertThatThrownBy(() -> translateClient.translate("xx", "yy", "foo bar"))
        .isInstanceOf(GoogleTranslateException.class)
        .hasMessage("""
            Google API error:
            HTTP Status: 500
            Response body:
            unknown error occurred
            """);
  }

  /**
   * This method intentionally duplicates method {@code tatbash.translation.google.GoogleTranslateClient#params(java.lang.String, java.lang.String, java.lang.String)}.
   * In this way they test each other.
   */
  private MultiValueMap<String, String> params(String sourceLanguageCode, String targetLanguageCode, String text) {
    final var params = new LinkedMultiValueMap<String, String>();
    params.add("client", "gtx");
    params.add("sl", sourceLanguageCode);
    params.add("tl", targetLanguageCode);
    params.add("dt", "t");
    params.add("ie", "UTF-8");
    params.add("oe", "UTF-8");
    params.add("otf", "1");
    params.add("ssel", "0");
    params.add("tsel", "0");
    params.add("kc", "7");
    params.add("dt", "at");
    params.add("dt", "bd");
    params.add("dt", "ex");
    params.add("dt", "ld");
    params.add("dt", "md");
    params.add("dt", "qca");
    params.add("dt", "rw");
    params.add("dt", "rm");
    params.add("dt", "ss");
    params.add("q", text);
    return params;
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
  @Import({ApplicationConfig.class, GoogleTranslateClient.class})
  static class TestConfig {

    @Autowired
    @Qualifier("googleTranslationRestTemplate")
    private RestTemplate restTemplate;

    @Bean
    MockRestServiceServer mockRestServiceServer() {
      final var customizer = new MockServerRestTemplateCustomizer();
      customizer.customize(this.restTemplate);
      return customizer.getServer();
    }
  }
}
