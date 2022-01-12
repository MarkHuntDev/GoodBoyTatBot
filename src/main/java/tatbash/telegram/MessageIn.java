package tatbash.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageIn {

  private final boolean hasMessage;
  private final String text;

  private MessageIn(boolean hasMessage, String text) {
    this.hasMessage = hasMessage;
    this.text = text;
  }

  public boolean hasMessage() {
    return hasMessage;
  }

  public String getText() {
    return text;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private boolean hasMessage;
    private String text;

    public Builder from(Update update) {
      if (update == null || update.getMessage() == null || update.getMessage().getText() == null) {
        return this;
      }
      this.hasMessage = true;
      this.text = update.getMessage().getText();
      return this;
    }

    public MessageIn build() {
      return new MessageIn(this.hasMessage, this.text);
    }
  }
}
