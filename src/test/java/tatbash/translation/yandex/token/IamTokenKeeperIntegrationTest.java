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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.ApplicationConfig;
import tatbash.infrastructure.config.YandexCloudTokenProperties;

@ActiveProfiles({"test"})
@RestClientTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration")
class IamTokenKeeperIntegrationTest {

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
      /*
        TODO: 1. Leave comment here to explain why I had to init mock server
                 during bean initialization due to scheduling.
              2. When I run full build refreshToken calls 3 times instead of 2.
                 Such behaviour should be fixed.
       */
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
