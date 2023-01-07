package tatbash.translation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.infrastructure.config.ApplicationProperties.LanguagePairProperty;
import tatbash.telegram.MessageIn;

class LanguagePairFinderTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_param_is_null(MessageIn messageIn, ApplicationProperties props, String message) {
    assertThatThrownBy(() -> new LanguagePairFinder(messageIn, props))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(null, null, "messageIn cannot be null"),
        Arguments.of(new MessageIn(1L, "text", Set.of()), null, "properties cannot be null")
    );
  }

  @ParameterizedTest
  @MethodSource("languagePairFindingCandidates")
  void should_find_language_pair(MessageIn messageIn, ApplicationProperties props, LanguagePairProperty pair) {
    assertThat(new LanguagePairFinder(messageIn, props).find())
        .isEqualTo(Optional.ofNullable(pair));
  }

  private static Stream<Arguments> languagePairFindingCandidates() {
    return Stream.of(
        Arguments.of(
            new MessageIn(1L, "test", Set.of("#hashtag2")),
            new ApplicationProperties(
                List.of(
                    new LanguagePairProperty("#hashtag1", "ru", "tt"),
                    new LanguagePairProperty("#hashtag2", "tt", "ru")
                )
            ),
            new LanguagePairProperty("#hashtag2", "tt", "ru")
        ),
        Arguments.of(
            new MessageIn(1L, "test", Set.of()),
            new ApplicationProperties(List.of()),
            null
        )
    );
  }
}
