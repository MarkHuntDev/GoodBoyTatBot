package tatbash.translation.yandex.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.ExpectedCount.between;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import lombok.RequiredArgsConstructor;
import org.awaitility.Durations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.ApplicationConfig;
import tatbash.infrastructure.config.YandexCloudTokenProperties;
import tatbash.infrastructure.smoketest.RestClientSmokeTest;

/**
 * <p>
 * This is non-trivial smoke test due to {@link Scheduled}
 * annotation using (see {@link IamTokenKeeper#refreshToken()}).
 * </p>
 * </br>
 * <p>
 * Before making requests to {@link MockRestServiceServer}, mocks have to be defined by
 * {@link MockRestServiceServer#expect(RequestMatcher)}. But using Spring Bean
 * with {@link Scheduled} annotation leads to difficult controlling behaviour. When {@link IamTokenKeeper}
 * bean created it sends http-request to a Mock Server immediately (before mocks are defined) and every time
 * after defined interval. Therefore, when test is started to execute, mocks haven't defined yet.
 * As a workaround Mock Server with mocks defining during Spring Bean initialization.
 * </p>
 * </br>
 * <p>
 *   There is a thread separated from the test which was ran by scheduler.
 *   It's required to extinguish that separated thread after test completed.
 *   For this purpose {@link DirtiesContext} used.
 * </p>
 */
@ExtendWith(SpringExtension.class)
@RestClientSmokeTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration"
    }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IamTokenKeeperSmokeTest {

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @SpyBean
  private IamTokenKeeper tokenKeeper;

  @Test
  void should_successfully_obtain_iam_token() {
    // given:
    await()
        .atMost(Durations.ONE_SECOND)
        .untilAsserted(() -> verify(tokenKeeper, atLeast(2)).refreshToken());

    // when:
    final var token =
        tokenKeeper.getIamToken();

    // then:
    assertThat(token)
        .isEqualTo("iam-token");
  }

  @AfterEach
  void tearDown() {
    this.mockRestServiceServer.verify();
  }

  @TestConfiguration
  @Import({ApplicationConfig.class})
  @RequiredArgsConstructor
  static class TestConfig {

    @Qualifier("yandexTokenRestTemplate")
    private final RestTemplate restTemplate;
    private final YandexCloudTokenProperties properties;

    @Bean
    @DependsOn("mockRestServiceServer")
    IamTokenKeeper iamTokenKeeper() {
      return new IamTokenKeeper(this.restTemplate, this.properties);
    }

    @Bean
    MockRestServiceServer mockRestServiceServer() {
      final var customizer = new MockServerRestTemplateCustomizer();
      customizer.customize(this.restTemplate);
      final var server = customizer.getServer();
      final var expectedJsonRequest = """
              {
                "yandexPassportOauthToken": "oauth-token"
              }
          """;
      final var expectedJsonResponse = """
               {
                 "iamToken": "iam-token",
                 "expiresAt": "2000-01-01T00:00:00.000Z"
               }
          """;
      server
          .expect(between(2, 3), requestTo("/tokens"))
          .andExpect(method(HttpMethod.POST))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(content().json(expectedJsonRequest))
          .andRespond(withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));
      return server;
    }
  }
}
