package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TelegramAuthPropertiesTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_any_param_is_illegal(String name,
                                                        String token,
                                                        String exceptionMessage) {
    assertThatThrownBy(() -> new TelegramAuthProperties(name, token))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, "x", "name can't be null or empty"),
        Arguments.of("x", null, "token can't be null or empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new TelegramAuthProperties("name", "token");
    assertThat(actual.name())
        .isEqualTo("name");
    assertThat(actual.token())
        .isEqualTo("token");
  }
}
