package tatbash.telegram;

import static tatbash.telegram.UpdateUtils.messageTextExists;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import tatbash.translation.TranslationService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationDelegator {

  private final TranslationService service;
  private final TelegramBotExecutor executor;

  void translate(Update update) {
    if (messageTextExists(update)) {
      final var messageOut = service.translate(MessageIn.from(update));
      if (messageOut.exists()) {
        executor.sendMessage(messageOut.toSendMessage());
      }
    }
  }
}
