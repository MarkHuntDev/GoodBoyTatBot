package tatbash.telegram;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractHashtags;
import static tatbash.telegram.UpdateUtils.extractRepliedText;
import static tatbash.telegram.UpdateUtils.extractText;

import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Chat message representation.
 */
public record MessageIn(Long chatId, String text, Set<String> hashtags, String repliedText) {

  public MessageIn(Long chatId, String text, Set<String> hashtags) {
    this(chatId, text, hashtags, null);
  }

  public MessageIn(Long chatId, String text, Set<String> hashtags, String repliedText) {
    this.chatId = requireNonNull(chatId, "chatId can't be null");
    this.text = requireNonNull(text, "text can't be null");
    this.hashtags = unmodifiableSet(requireNonNull(hashtags, "hashtags can't be null"));
    this.repliedText = StringUtils.trimToNull(repliedText);
  }

  public boolean isReply() {
    return this.repliedText != null;
  }

  public Set<String> hashtags() {
    return unmodifiableSet(this.hashtags);
  }

  /**
   * Safely converts {@link Update} to {@link MessageIn},
   * performing null checks.
   */
  public static MessageIn from(Update update) {
    return new MessageIn(
        extractChatId(update), extractText(update), extractHashtags(update), extractRepliedText(update)
    );
  }
}
