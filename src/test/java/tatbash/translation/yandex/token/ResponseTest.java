package tatbash.translation.yandex.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResponseTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_any_param_is_null(String iamToken, OffsetDateTime expiresAt,
                                                     String exceptionMessage) {
    assertThatThrownBy(() -> new Response(iamToken, expiresAt))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(null, null, "iamToken can't be null"),
        Arguments.of("x", null, "expiresAt can't be null")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var expectedOffsetDateTime = "2000-01-01T00:00+00:00";
    final var actual = new Response("x", OffsetDateTime.parse(expectedOffsetDateTime));
    assertThat(actual.iamToken())
        .isEqualTo("x");
    assertThat(actual.expiresAt())
        .isEqualTo(OffsetDateTime.parse(expectedOffsetDateTime));
  }
}