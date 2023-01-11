package tatbash.translation.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tatbash.translation.utils.Utils.escapeQuotes;
import static tatbash.translation.utils.Utils.quote;
import static tatbash.translation.utils.Utils.unescapeQuotes;
import static tatbash.translation.utils.Utils.unquote;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class UtilsTest {

  @Test
  void should_return_quoted_string() {
    final var actual = quote("example");
    assertThat(actual)
        .isEqualTo("\"example\"");
  }

  @Test
  void should_return_unquoted_string() {
    final var actual = unquote("\"example\"");
    assertThat(actual)
        .isEqualTo("example");
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