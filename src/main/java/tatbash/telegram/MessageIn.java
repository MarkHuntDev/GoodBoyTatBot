package tatbash.telegram;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

public record MessageIn(boolean exists, String text, Set<String> markers) {

  public MessageIn(boolean exists, String text, Set<String> markers) {
    this.exists = exists;
    this.text = text;
    this.markers = (markers == null ? emptySet() : unmodifiableSet(markers));
  }

  @Override
  public Set<String> markers() {
    return unmodifiableSet(this.markers);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private boolean exists;
    private String text;
    private Set<String> markers;

    public Builder from(Update update) {
      this.exists = messageExists(update);
      if (!this.exists) {
        return this;
      }
      this.text = extractText(update);
      this.markers = extractMarkers(update);
      return this;
    }

    public MessageIn build() {
      return new MessageIn(this.exists, this.text, this.markers);
    }

    private boolean messageExists(Update update) {
      return update != null
          && update.getMessage() != null
          && update.getMessage().getText() != null;
    }

    private String extractText(Update update) {
      return update.getMessage().getText();
    }

    private Set<String> extractMarkers(Update update) {
      if (CollectionUtils.isEmpty(update.getMessage().getEntities())) {
        return emptySet();
      }
      return update.getMessage().getEntities()
          .stream()
          .filter(Objects::nonNull)
          .filter(this::isHashtag)
          .map(MessageEntity::getText)
          .collect(toSet());
    }

    private boolean isHashtag(MessageEntity entity) {
      return "hashtag".equals(entity.getType());
    }
  }
}
