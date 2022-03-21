package tatbash.telegram;

import static java.util.Objects.requireNonNull;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
class UpdateUtils {

  static boolean messageTextExists(Update update) {
    return update != null
        && update.getMessage() != null
        && update.getMessage().getText() != null;
  }

  static Long extractChatId(Update update) {
    return nonNullMessage(update).getChatId();
  }

  private static Message nonNullMessage(Update update) {
    return requireNonNull(nonNullUpdate(update).getMessage(), "message object can't be null");
  }

  private static Update nonNullUpdate(Update update) {
    return requireNonNull(update, "update object can't be null");
  }
}
