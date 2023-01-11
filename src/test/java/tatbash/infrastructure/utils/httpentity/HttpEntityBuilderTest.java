package tatbash.infrastructure.utils.httpentity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import tatbash.translation.utils.httpentity.HttpEntityBuilder;
import tatbash.translation.yandex.translate.Request;

class HttpEntityBuilderTest {

  @Test
  void should_build_http_entity_successfully() {
    // when:
    final var expectedBody = new Request("test-folder-id", "ru", "tt", new String[] {"привет"});

    final var httpEntity = HttpEntityBuilder.builder()
        .withBody(expectedBody)
        .withHeader("Authorization", List.of("Bearer iam-token"))
        .withHeader("X-one-more-header", List.of("some-value", "another-value"))
        .build();

    // then:
    assertThat(httpEntity.getBody())
        .isEqualTo(expectedBody);

    final var headerPairs = httpEntity.getHeaders().entrySet()
        .stream()
        .map(entry -> new ImmutablePair<>(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    assertThat(headerPairs)
        .containsExactlyInAnyOrder(
            new ImmutablePair<>("Authorization", List.of("Bearer iam-token")),
            new ImmutablePair<>("X-one-more-header", List.of("some-value", "another-value"))
        );
  }
}