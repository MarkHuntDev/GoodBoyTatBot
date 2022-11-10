package tatbash.telegram;

import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractText;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public record MessageOut(Long chatId, String text) {

  public MessageOut {
    Objects.requireNonNull(chatId, "chatId can't be null");
    Objects.requireNonNull(text, "text can't be null");
  }

  public boolean exists() {
    return StringUtils.isNotBlank(this.text);
  }

  public SendMessage toSendMessage() {
    SendMessage response = new SendMessage();
    response.setChatId(this.chatId.toString());
    response.setText(this.text);
    return response;
  }

  public static MessageOut of(Update update) {
    return new MessageOut(extractChatId(update), extractText(update));
  }
}
