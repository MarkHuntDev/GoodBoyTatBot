package tatbash.translation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tatbash.telegram.MessageIn;
class MessageRefinerTest {

  @Test
  void should_throw_exception_when_param_is_null() {
    assertThatThrownBy(() -> new MessageRefiner(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("messageIn cannot be null");
  }

  @ParameterizedTest
  @MethodSource("refiningTextCandidates")
  void should_return_refined_text_when_exists(String originalText,
                                              String repliedOriginalText,
                                              String hashtag,
                                              String refinedText) {
    final var refiner = new MessageRefiner(new MessageIn(1L, originalText, Set.of(hashtag), repliedOriginalText));
    assertThat(refiner.getRefinedMessage())
        .isEqualTo(Optional.ofNullable(refinedText));
  }

  private static Stream<Arguments> refiningTextCandidates() {
    return Stream.of(
        Arguments.of("test1 #hashtag", null, "#hashtag", "test1"),
        Arguments.of("#hashtag", null, "#hashtag", null),
        Arguments.of("#hashtag", "test2 #hashtag", "#hashtag", "test2"),
        Arguments.of("#hashtag", "#hashtag", "#hashtag", null)
    );
  }
}
