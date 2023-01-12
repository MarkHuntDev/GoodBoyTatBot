package tatbash.translation.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tatbash.translation.utils.Utils.escapeQuotes;
import static tatbash.translation.utils.Utils.quote;
import static tatbash.translation.utils.Utils.unescapeQuotes;
import static tatbash.translation.utils.Utils.unquote;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UtilsTest {

  @ParameterizedTest
  @MethodSource("quoteCandidates")
  void should_return_quoted_string(String raw, String expected) {
    final var actual = quote(raw);
    assertThat(actual)
        .isEqualTo(expected);
  }

  private static Stream<Arguments> quoteCandidates() {
    return Stream.of(
        Arguments.of("example", "\"example\""),
        Arguments.of("\"example\"", "\"\"example\"\"")
    );
  }

  @ParameterizedTest
  @MethodSource("unquoteCandidates")
  void should_return_unquoted_string() {
    final var actual = unquote("\"example\"");
    assertThat(actual)
        .isEqualTo("example");
  }

  private static Stream<Arguments> unquoteCandidates() {
    return Stream.of(
        Arguments.of("\"example\"", "example"),
        Arguments.of("\"\"example\"\"", "\"example\"")
    );
  }

  @Test
  void should_return_escaped_string() {
    final var actual = escapeQuotes("example with \"quoted\" string");
    assertThat(actual)
        .isEqualTo("example with \\\"quoted\\\" string");
  }

  @Test
  void should_return_unescaped_string() {
    final var actual = unescapeQuotes("example with \\\"quoted\\\" string");
    assertThat(actual)
        .isEqualTo("example with \"quoted\" string");
  }

  @Test
  void should_has_private_constructor() throws NoSuchMethodException {
    final var constructor = Utils.class.getDeclaredConstructor();
    assertThat(Modifier.isPrivate(constructor.getModifiers()))
        .isTrue();
    constructor.setAccessible(true);
    assertThatThrownBy(constructor::newInstance)
        .hasRootCauseInstanceOf(UnsupportedOperationException.class)
        .hasRootCauseMessage("This is a utility class and cannot be instantiated");
  }
}