package tatbash.telegram;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
class UpdateUtils {

  static Set<String> extractHashtags(Update update) {
    final var message = nonNullMessage(update);
    if (CollectionUtils.isEmpty(message.getEntities())) {
      return emptySet();
    }
    return message.getEntities()
        .stream()
        .filter(Objects::nonNull)
        .filter(UpdateUtils::isHashtag)
        .map(MessageEntity::getText)
        .collect(toSet());
  }

  private static boolean isHashtag(MessageEntity entity) {
    return "hashtag".equals(entity.getType());
  }

  static boolean messageTextExists(Update update) {
    return update != null
        && update.getMessage() != null
        && update.getMessage().getText() != null;
  }

  static Long extractChatId(Update update) {
    return nonNullMessage(update).getChatId();
  }

  static String extractText(Update update) {
    return requireNonNull(nonNullMessage(update).getText(), "message text can't be null");
  }

  private static Message nonNullMessage(Update update) {
    return requireNonNull(nonNullUpdate(update).getMessage(), "message object can't be null");
  }

  private static Update nonNullUpdate(Update update) {
    return requireNonNull(update, "update object can't be null");
  }
}
