package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tatbash.infrastructure.config.ApplicationProperties.LanguagePairProperty;

class ApplicationPropertiesTest {

  @Test
  void should_throw_exception_when_param_is_null() {
    assertThatThrownBy(() -> new ApplicationProperties(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("hashtags can't be null");
  }

  @Test
  void should_create_application_properties_instance_successfully() {
    assertThat(new ApplicationProperties(Collections.emptyList()).hashtags())
        .isEmpty();
  }

  @ParameterizedTest
  @MethodSource("candidates")
  void should_throw_exception_when_any_param_is_illegal(String hashtag, String sourceLanguage,
                                                        String targetLanguage, String message) {
    assertThatThrownBy(() -> new LanguagePairProperty(hashtag, sourceLanguage, targetLanguage))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidates() {
    return Stream.of(
        Arguments.of(null, "y", "z", "hashtag can't be null or empty"),
        Arguments.of("x", null, "z", "sourceLanguage can't be null or empty"),
        Arguments.of("x", "y", null, "targetLanguage can't be null or empty")
    );
  }

  @Test
  void should_create_hashtag_property_instance_successfully() {
    final var actual = new LanguagePairProperty("x", "y", "z");
    assertThat(actual.hashtag())
        .isEqualTo("x");
    assertThat(actual.sourceLanguage())
        .isEqualTo("y");
    assertThat(actual.targetLanguage())
        .isEqualTo("z");
  }
}
