package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ApplicationPropertiesTest {

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_null_or_empty_collection(Set<String> markers) {
    assertThatThrownBy(() -> new ApplicationProperties(markers))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("markers can't be null or empty");

  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of((Set<String>) null),
        Arguments.of(new HashSet<>())
    );
  }

}
