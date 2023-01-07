package tatbash.translation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.telegram.MessageIn;
import tatbash.telegram.MessageOut;
import tatbash.translation.yandex.translate.YandexTranslateClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

  private final ApplicationProperties properties;
  private final YandexTranslateClient client;

  /**
   * Translate via Yandex API.
   */
  public MessageOut translate(MessageIn messageIn) {
    final var refined = new MessageRefiner(messageIn).getRefinedMessage();
    if (refined.isEmpty()) {
      return MessageOut.empty(messageIn.chatId());
    }
    final var languages = new LanguagePairFinder(messageIn, properties).find();
    if (languages.isEmpty()) {
      return MessageOut.empty(messageIn.chatId());
    }
    final String translatedText = client.translate(
        languages.get().sourceLanguage(),
        languages.get().targetLanguage(),
        refined.get()
    );
    return new MessageOut(messageIn.chatId(), translatedText);
  }
}
