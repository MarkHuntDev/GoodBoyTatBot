package tatbash.translation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Optional;
import tatbash.infrastructure.config.ApplicationProperties;
import tatbash.infrastructure.config.ApplicationProperties.LanguagePairProperty;
import tatbash.telegram.MessageIn;

class LanguagePairFinder {

  private final MessageIn messageIn;
  private final ApplicationProperties properties;

  LanguagePairFinder(MessageIn messageIn, ApplicationProperties properties) {
    this.messageIn = requireNonNull(messageIn, "messageIn cannot be null");
    this.properties = requireNonNull(properties, "properties cannot be null");
  }

  Optional<LanguagePairProperty> find() {
    if (messageIn.hashtags().isEmpty()) {
      return Optional.empty();
    }
    final var hashtag = new ArrayList<>(messageIn.hashtags()).get(0);
    return properties
        .hashtags()
        .stream()
        .filter(item -> item.hashtag().equals(hashtag))
        .findFirst();
  }
}
