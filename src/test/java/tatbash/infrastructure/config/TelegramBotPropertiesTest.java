package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TelegramBotPropertiesTest {

  @ParameterizedTest
  @MethodSource("illegalParamsCandidates")
  void should_throw_exception_when_any_param_is_illegal(String name,
                                                        String token,
                                                        Class<RuntimeException> exceptionClass,
                                                        String exceptionMessage) {
    assertThatThrownBy(() -> new TelegramBotProperties(name, token))
        .isInstanceOf(exceptionClass)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> illegalParamsCandidates() {
    return Stream.of(
        Arguments.of(null, "x", NullPointerException.class, "name can't be null"),
        Arguments.of("x", null, NullPointerException.class, "token can't be null"),
        Arguments.of("", "x", IllegalArgumentException.class, "name can't be empty"),
        Arguments.of("x", "", IllegalArgumentException.class, "token can't be empty"),
        Arguments.of(" ", "x", IllegalArgumentException.class, "name can't be empty"),
        Arguments.of("x", " ", IllegalArgumentException.class, "token can't be empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new TelegramBotProperties("name", "token");
    assertThat(actual.name())
        .isEqualTo("name");
    assertThat(actual.token())
        .isEqualTo("token");
  }
}
