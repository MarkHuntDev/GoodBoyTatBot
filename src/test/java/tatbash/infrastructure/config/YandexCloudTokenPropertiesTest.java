package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class YandexCloudTokenPropertiesTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_any_param_is_illegal(String url, String authorizationToken, String message) {
    assertThatThrownBy(() -> new YandexCloudTokenProperties(url, authorizationToken, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, "x", "url can't be null or empty"),
        Arguments.of("x", null, "authorizationToken can't be null or empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new YandexCloudTokenProperties("x", "y", 99);
    assertThat(actual.url())
        .isEqualTo("x");
    assertThat(actual.authorizationToken())
        .isEqualTo("y");
    assertThat(actual.delayBeforeRefreshInMillis())
        .isEqualTo(99);
  }
}