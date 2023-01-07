package tatbash.translation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Optional;
import tatbash.telegram.MessageIn;

class MessageRefiner {

  private final MessageIn messageIn;

  MessageRefiner(MessageIn messageIn) {
    this.messageIn = requireNonNull(messageIn, "messageIn cannot be null");
  }

  Optional<String> getRefinedMessage() {
    if (messageIn.hashtags().isEmpty()) {
      return Optional.empty();
    }
    final var hashtag = new ArrayList<>(messageIn.hashtags()).get(0);
    final var refined = messageIn.text().replace(hashtag, "");
    if (refined.isBlank()) {
      if (messageIn.isReply()) {
        return getRefinedReplyMessage(hashtag);
      }
    } else {
      return Optional.of(refined.trim());
    }
    return Optional.empty();
  }

  private Optional<String> getRefinedReplyMessage(String hashtag) {
    final var refinedReply = messageIn.repliedText().replace(hashtag, "");
    if (refinedReply.isBlank()) {
      return Optional.empty();
    } else {
      return Optional.of(refinedReply.trim());
    }
  }
}
