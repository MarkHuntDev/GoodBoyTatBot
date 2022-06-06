package tatbash.telegram;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
class FixtureUtils {

  /**
   * Chained initializer for {@link Update}.
   */
  static class UpdateBuilder {

    private final Update update = new Update();

    UpdateBuilder setMessage(Message message) {
      this.update.setMessage(message);
      return this;
    }

    Update build() {
      return this.update;
    }
  }

  /**
   * Chained initializer for {@link Message}.
   */
  static class MessageBuilder {

    private final Message message = new Message();
    private final List<MessageEntity> entities = new ArrayList<>();

    MessageBuilder setText(String text) {
      this.message.setText(text);
      return this;
    }

    MessageBuilder setChat(Chat chat) {
      this.message.setChat(chat);
      return this;
    }

    MessageBuilder addMessageEntity(MessageEntity entity) {
      this.entities.add(entity);
      return this;
    }

    Message build() {
      this.message.setEntities(new ArrayList<>(this.entities));
      return this.message;
    }
  }

  /**
   * Chained initializer for {@link Chat}.
   */
  static class ChatBuilder {

    private final Chat chat = new Chat();

    ChatBuilder setId(Long id) {
      this.chat.setId(id);
      return this;
    }

    Chat build() {
      return this.chat;
    }
  }

  /**
   * Chained initializer for {@link MessageEntity}.
   */
  static class MessageEntityBuilder {

    private final MessageEntity entity = new MessageEntity();

    MessageEntityBuilder setType(String type) {
      this.entity.setType(type);
      return this;
    }

    MessageEntityBuilder setText(String text) {
      this.entity.setText(text);
      return this;
    }

    MessageEntity build() {
      return this.entity;
    }
  }
}
