package tatbash.telegram;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tatbash.infrastructure.config.TelegramBotProperties;
import tatbash.translation.TranslationService;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramBot extends TelegramLongPollingBot {

  private final TelegramBotProperties properties;
  private final TranslationService service;

  @Override
  @SneakyThrows(TelegramApiException.class)
  public void onUpdateReceived(Update update) {
    final var message = MessageIn
        .builder()
        .from(update)
        .build();
    if (message.exists()) {
      execute(service.foo(update));
    }
  }

  @PostConstruct
  @SneakyThrows({TelegramApiRequestException.class, TelegramApiException.class})
  public void registerBot() {
    new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
  }

  @Override
  public String getBotUsername() {
    return this.properties.name();
  }

  @Override
  public String getBotToken() {
    return this.properties.token();
  }
}
