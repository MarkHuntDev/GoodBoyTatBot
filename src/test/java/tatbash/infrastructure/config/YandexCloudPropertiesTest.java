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
  void should_throw_exception_when_any_param_is_illegal(String folderId,
                                                        String authorizationToken,
                                                        Class<RuntimeException> exceptionClass,
                                                        String message) {
    assertThatThrownBy(() -> new YandexCloudProperties(folderId, authorizationToken))
        .isInstanceOf(exceptionClass)
        .hasMessage(message);
  }

  private static Stream<Arguments> illegalParamsCandidates() {
    return Stream.of(
        Arguments.of(null, "x", NullPointerException.class, "folderId can't be null"),
        Arguments.of("x", null, NullPointerException.class, "authorizationToken can't be null"),
        Arguments.of("", "x", IllegalArgumentException.class, "folderId can't be empty"),
        Arguments.of("x", "", IllegalArgumentException.class, "authorizationToken can't be empty"),
        Arguments.of(" ", "x", IllegalArgumentException.class, "folderId can't be empty"),
        Arguments.of("x", " ", IllegalArgumentException.class, "authorizationToken can't be empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new YandexCloudProperties("x", "y");
    assertThat(actual.folderId())
        .isEqualTo("x");
    assertThat(actual.authorizationToken())
        .isEqualTo("y");
  }
}
