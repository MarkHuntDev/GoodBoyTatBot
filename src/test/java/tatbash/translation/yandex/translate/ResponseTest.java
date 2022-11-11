package tatbash.translation.yandex.translate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tatbash.translation.yandex.translate.Response;
import tatbash.translation.yandex.translate.Response.TranslationText;

class ResponseTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_any_param_is_null(TranslationText[] translations, String exceptionMessage) {
    assertThatThrownBy(() -> new Response(translations))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(new TranslationText[] {null}, "translations can't contain null elements"),
        Arguments.of(null, "translations can't be null")
    );
  }

  @Test
  void should_throw_exception_when_text_is_null() {
    assertThatThrownBy(() -> new TranslationText(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("text can't be null");
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new Response(new TranslationText[] {new TranslationText("test")});
    assertThat(actual.translations())
        .containsExactly(new TranslationText("test"));
  }
}
