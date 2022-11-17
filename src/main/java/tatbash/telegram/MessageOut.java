package tatbash.telegram;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Bot's answer message representation.
 */
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

  public static MessageOut empty(Long chatId) {
    return new MessageOut(chatId, "");
  }
}
