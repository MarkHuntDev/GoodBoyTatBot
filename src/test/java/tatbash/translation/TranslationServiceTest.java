package tatbash.translation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.infrastructure.config.ApplicationProperties.LanguagePairProperty;
import tatbash.telegram.MessageIn;
import tatbash.translation.yandex.translate.YandexTranslateClient;

class TranslationServiceTest {

  private TranslationService service;
  private YandexTranslateClient translateClient;

  @BeforeEach
  void setup() {
    final var properties = new ApplicationProperties(List.of(new LanguagePairProperty("#hashtag", "x", "y")));
    this.translateClient = mock(YandexTranslateClient.class);
    this.service = new TranslationService(properties, this.translateClient);
  }

  @ParameterizedTest
  @MethodSource("messageInCandidates")
  void when_hashtag_is_unknown_or_does_not_exist(Set<String> hashtags, boolean exists) {
    final var messageOut = service.translate(new MessageIn(1L, "test", hashtags));
    assertThat(messageOut.chatId())
        .isEqualTo(1L);
    assertThat(messageOut.exists())
        .isEqualTo(exists);
  }

  private static Stream<Arguments> messageInCandidates() {
    return Stream.of(
        Arguments.of(Set.of(), false),
        Arguments.of(Set.of("#unknown"), false)
    );
  }

  @Test
  void when_successfully_translated() {
    // given:
    when(translateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenReturn("text");
    // when:
    final var messageOut = service.translate(new MessageIn(1L, "text #hashtag", Set.of("#hashtag")));
    // then:
    assertThat(messageOut.chatId())
        .isEqualTo(1L);
    assertThat(messageOut.text())
        .isEqualTo("text");
  }
}