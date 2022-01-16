package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TelegramBotPropertiesTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_blank_params(String name, String token) {
    assertThatThrownBy(() -> new TelegramBotProperties(name, token))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("name and token can't be null or empty");

  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of("", ""),
        Arguments.of(" ", " ")
    );
  }
}