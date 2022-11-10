package tatbash.telegram;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractHashtags;
import static tatbash.telegram.UpdateUtils.extractText;

import java.util.Set;
import org.telegram.telegrambots.meta.api.objects.Update;

public record MessageIn(Long chatId, String text, Set<String> hashtags) {

  public MessageIn(Long chatId, String text, Set<String> hashtags) {
    this.chatId = requireNonNull(chatId, "chatId can't be null");
    this.text = requireNonNull(text, "text can't be null");
    this.hashtags = unmodifiableSet(requireNonNull(hashtags, "hashtags can't be null"));
  }

  public Set<String> hashtags() {
    return unmodifiableSet(this.hashtags);
  }

  public static MessageIn of(Update update) {
    return new MessageIn(extractChatId(update), extractText(update), extractHashtags(update));
  }
}
