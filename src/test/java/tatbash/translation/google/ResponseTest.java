package tatbash.translation.google;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResponseTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_any_param_is_illegal(String translated, String message) {
    assertThatThrownBy(() -> new Response(translated))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, "translated can't be null or empty"),
        Arguments.of("", "translated can't be null or empty")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new Response("x");
    assertThat(actual.translated())
        .isEqualTo("x");
  }
}