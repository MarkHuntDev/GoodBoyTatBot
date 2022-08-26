package tatbash.telegram;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractHashtags;
import static tatbash.telegram.UpdateUtils.extractText;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

public record MessageIn(Long chatId, String text, List<String> hashtags) {

  public MessageIn(Long chatId, String text, List<String> hashtags) {
    this.chatId = requireNonNull(chatId, "chatId can't be null");
    this.text = requireNonNull(text, "text can't be null");
    this.hashtags = unmodifiableList(requireNonNull(hashtags, "hashtags can't be null"));
  }

  public List<String> hashtags() {
    return unmodifiableList(this.hashtags);
  }

  public static MessageIn of(Update update) {
    return new MessageIn(extractChatId(update), extractText(update), extractHashtags(update));
  }
}
