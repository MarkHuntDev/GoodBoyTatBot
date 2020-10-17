package tatbash.telegram;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import tatbash.infrastructure.config.TelegramBotProperties;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

  private final TelegramBotProperties properties;

  @PostConstruct
  public void selfRegister() throws TelegramApiRequestException {
    new TelegramBotsApi().registerBot(this);
  }

  @Override
  public void onUpdateReceived(Update update) {
    try {
      SendMessage response = new SendMessage();
      response.setChatId(update.getMessage().getChatId());
      response.setText(update.getMessage().getText());
      execute(response);
    } catch (TelegramApiException ex) {
      ex.printStackTrace();
      log.error(ex.getLocalizedMessage(), ex);
    }
  }

  @Override
  public String getBotUsername() {
    return this.properties.getName();
  }

  @Override
  public String getBotToken() {
    return this.properties.getToken();
  }
}
