package tatbash.translation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
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
import tatbash.infrastructure.exception.GoogleTranslateException;
import tatbash.infrastructure.exception.YandexTranslateException;
import tatbash.telegram.MessageIn;
import tatbash.translation.google.GoogleTranslateClient;
import tatbash.translation.yandex.translate.YandexTranslateClient;

class TranslationServiceTest {

  private TranslationService service;
  private YandexTranslateClient yandexTranslateClient;
  private GoogleTranslateClient googleTranslateClient;

  @BeforeEach
  void setup() {
    final var properties = new ApplicationProperties(List.of(new LanguagePairProperty("#hashtag", "x", "y")));
    this.yandexTranslateClient = mock(YandexTranslateClient.class);
    this.googleTranslateClient = mock(GoogleTranslateClient.class);
    this.service = new TranslationService(properties, this.yandexTranslateClient, this.googleTranslateClient);
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
    when(googleTranslateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenReturn("text");
    // when:
    final var messageOut = service.translate(new MessageIn(1L, "text #hashtag", Set.of("#hashtag")));
    // then:
    verifyNoInteractions(yandexTranslateClient);
    assertThat(messageOut.chatId())
        .isEqualTo(1L);
    assertThat(messageOut.text())
        .isEqualTo("text");
  }

  @Test
  void when_hashtag_without_text_used_then_translation_should_not_be_performed() {
    // when:
    final var messageOut = service.translate(new MessageIn(1L, " #hashtag ", Set.of("#hashtag")));
    // then:
    verifyNoInteractions(googleTranslateClient);
    verifyNoInteractions(yandexTranslateClient);
    assertThat(messageOut.exists())
        .isFalse();
  }

  @Test
  void when_replied_text_should_be_translated() {
    // given:
    when(googleTranslateClient.translate(eq("x"), eq("y"), eq("replied text")))
        .thenReturn("translated replied text");
    // when:
    final var messageOut = service.translate(new MessageIn(1L, " #hashtag ", Set.of("#hashtag"), "replied text"));
    // then:
    verifyNoInteractions(yandexTranslateClient);
    assertThat(messageOut.exists())
        .isTrue();
    assertThat(messageOut.text())
        .isEqualTo("translated replied text");
  }

  @Test
  void second_client_should_successfully_translate_when_first_client_failed() {
    // given:
    when(googleTranslateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenThrow(new GoogleTranslateException("some error"));
    when(yandexTranslateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenReturn("translated text");
    // when:
    final var messageOut = service.translate(new MessageIn(1L, "text #hashtag ", Set.of("#hashtag")));
    // then:
    assertThat(messageOut.exists())
        .isTrue();
    assertThat(messageOut.text())
        .isEqualTo("translated text");
  }

  @Test
  void when_both_clients_failed() {
    // given:
    when(googleTranslateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenThrow(new GoogleTranslateException("some error"));
    when(yandexTranslateClient.translate(eq("x"), eq("y"), eq("text")))
        .thenThrow(new YandexTranslateException("some error"));
    // then:
    assertThatThrownBy(() -> service.translate(new MessageIn(1L, "text #hashtag ", Set.of("#hashtag"))))
        .isInstanceOf(YandexTranslateException.class)
        .hasMessage("some error");
  }
}
