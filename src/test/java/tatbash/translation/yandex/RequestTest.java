package tatbash.translation.yandex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_any_param_is_null(String folderId,
                                                     String sourceLanguageCode,
                                                     String targetLanguageCode,
                                                     String[] texts,
                                                     String exceptionMessage) {
    assertThatThrownBy(() -> new Request(folderId, sourceLanguageCode, targetLanguageCode, texts))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(null, null, null, null, "folderId can't be null"),
        Arguments.of("", null, null, null, "sourceLanguageCode can't be null"),
        Arguments.of("", "", null, null, "targetLanguageCode can't be null"),
        Arguments.of("", "", "", null, "texts can't be null")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new Request("test", "sourceLang", "targetLang", new String[] {"1", "2"});
    assertThat(actual.folderId())
        .isEqualTo("test");
    assertThat(actual.sourceLanguageCode())
        .isEqualTo("sourceLang");
    assertThat(actual.targetLanguageCode())
        .isEqualTo("targetLang");
    assertThat(actual.texts())
        .contains("1", "2");
  }
}
