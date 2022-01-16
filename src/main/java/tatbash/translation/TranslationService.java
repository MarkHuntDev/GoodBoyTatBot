package tatbash.translation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tatbash.infrastructure.config.ApplicationProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

  private final ApplicationProperties properties;

  // todo: Сюда приходит update
  //       2. Проверяем в тексте наличие маркера
  //       3. Если есть маркер #перевод, то выдираем текст для перевода
  //       4. Выдранный текст отправляем на перевод в translation.yandex

  public SendMessage foo(Update update) {
    SendMessage response = new SendMessage();
    response.setChatId(update.getMessage().getChatId().toString());
    response.setText(update.getMessage().getText());
    return response;
  }

}
