package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class YandexCloudPropertiesTest {

  @ParameterizedTest
  @MethodSource("illegalParamsCandidates")
  void should_throw_exception_when_any_param_is_illegal(String translationUrl,
                                                        String folderId,
                                                        String authorizationToken,
                                                        Class<RuntimeException> exceptionClass,
                                                        String message) {
    assertThatThrownBy(() -> new YandexCloudProperties(translationUrl, folderId, authorizationToken))
        .isInstanceOf(exceptionClass)
        .hasMessage(message);
  }

  private static Stream<Arguments> illegalParamsCandidates() {
    return Stream.of(
        Arguments.of(null, "x", "x", NullPointerException.class, "translationUrl can't be null"),
        Arguments.of("x", null, "x", NullPointerException.class, "folderId can't be null"),
        Arguments.of("x", "x", null, NullPointerException.class, "authorizationToken can't be null"),
        Arguments.of("", "x", "x", IllegalArgumentException.class, "translationUrl can't be empty"),
        Arguments.of("x", "", "x", IllegalArgumentException.class, "folderId can't be empty"),
        Arguments.of("x", "x", "", IllegalArgumentException.class, "authorizationToken can't be empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new YandexCloudProperties("x", "y", "z");
    assertThat(actual.translationUrl())
        .isEqualTo("x");
    assertThat(actual.folderId())
        .isEqualTo("y");
    assertThat(actual.authorizationToken())
        .isEqualTo("z");
  }
}
