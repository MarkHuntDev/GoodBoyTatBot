package tatbash.telegram;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tatbash.infrastructure.config.TelegramAuthProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramBotExecutor extends TelegramLongPollingBot {

  private final TelegramAuthProperties properties;
  private final TranslationDelegator delegator;

  @PostConstruct
  @SneakyThrows({TelegramApiRequestException.class, TelegramApiException.class})
  public void registerBot() {
    new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
  }

  @Override
  public void onUpdateReceived(Update update) {
    delegator.translate(update);
  }

  @SneakyThrows(TelegramApiException.class)
  public void sendMessage(SendMessage message) {
    execute(message);
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
