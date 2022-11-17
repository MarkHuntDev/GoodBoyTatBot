package tatbash.translation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.infrastructure.config.ApplicationProperties.LanguagePairProperty;
import tatbash.telegram.MessageIn;
import tatbash.telegram.MessageOut;
import tatbash.translation.yandex.translate.YandexTranslateClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

  private final ApplicationProperties properties;
  private final YandexTranslateClient translateClient;

  /**
   * Translate via Yandex API.
   */
  public MessageOut translate(MessageIn messageIn) {
    final var hashtag = extractHashtag(messageIn);
    if (hashtag.isEmpty()) {
      return MessageOut.empty(messageIn.chatId());
    }
    final var languages = extractLanguages(hashtag.get());
    if (languages.isEmpty()) {
      return MessageOut.empty(messageIn.chatId());
    }
    final var textWithoutHashtag = messageIn.text().replaceAll(hashtag.get(), "");
    final var translatedText = translateClient.translate(
        languages.get().sourceLanguage(),
        languages.get().targetLanguage(),
        textWithoutHashtag.trim()
    );
    return new MessageOut(messageIn.chatId(), translatedText);
  }

  private Optional<String> extractHashtag(MessageIn messageIn) {
    return messageIn
        .hashtags()
        .stream()
        .findFirst();
  }

  private Optional<LanguagePairProperty> extractLanguages(String hashtag) {
    return properties
        .hashtags()
        .stream()
        .filter(item -> item.hashtag().equals(hashtag))
        .findFirst();
  }
}
