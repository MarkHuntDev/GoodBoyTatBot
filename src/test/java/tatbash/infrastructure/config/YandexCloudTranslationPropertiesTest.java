package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class YandexCloudTranslationPropertiesTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_any_param_is_illegal(String url, String folderId, String message) {
    assertThatThrownBy(() -> new YandexCloudTranslationProperties(url, folderId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, "x", "url can't be null or empty"),
        Arguments.of("x", null, "folderId can't be null or empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new YandexCloudTranslationProperties("x", "y");
    assertThat(actual.url())
        .isEqualTo("x");
    assertThat(actual.folderId())
        .isEqualTo("y");
  }
}
