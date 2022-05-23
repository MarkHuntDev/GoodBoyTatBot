package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractHashtags;
import static tatbash.telegram.UpdateUtils.extractText;
import static tatbash.telegram.UpdateUtils.messageTextExists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

class UpdateUtilsTest {

  @Test
  void should_return_extracted_chat_id() {
    final var expectedChatId = 999L;
    final var actualChatId = extractChatId(
        new HandyUpdate()
            .setMessage(
                new HandyMessage()
                    .setChat(
                        new HandyChat()
                            .setId(expectedChatId)
                            .chat()
                    )
                    .message()
            )
            .update()
    );
    assertThat(actualChatId).isEqualTo(expectedChatId);
  }

  @ParameterizedTest
  @MethodSource("candidatesWithAbsentChatId")
  void should_throw_exception_when_update_does_not_contain_chat_id(Update update, String message) {
    assertThatThrownBy(() -> extractChatId(update))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidatesWithAbsentChatId() {
    return Stream.of(
        Arguments.of(null, "update object can't be null"),
        Arguments.of(
            new HandyUpdate()
                .setMessage(null)
                .update(), "message object can't be null"
        )
    );
  }

  @Test
  void should_return_extracted_text() {
    final var expectedText = "test text";
    final var actualChatId = extractText(
        new HandyUpdate()
            .setMessage(
                new HandyMessage()
                    .setText(expectedText)
                    .message()
            )
            .update()
    );
    assertThat(actualChatId).isEqualTo(expectedText);
  }

  @ParameterizedTest
  @MethodSource("candidatesWithAbsentTextAndWithErrorMessage")
  void should_throw_exception_when_update_does_not_contain_text(Update update, String message) {
    assertThatThrownBy(() -> extractText(update))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidatesWithAbsentTextAndWithErrorMessage() {
    return Stream.of(
        Arguments.of(null, "update object can't be null"),
        Arguments.of(
            new HandyUpdate()
                .setMessage(null)
                .update(), "message object can't be null"
        ),
        Arguments.of(
            new HandyUpdate()
                .setMessage(
                    new HandyMessage()
                        .setText(null)
                        .message()
                )
                .update(), "message text can't be null"
        )
    );
  }

  @Test
  void should_return_extracted_hashtags() {
    final var expectedHashtag1 = "#hashtag1";
    final var expectedHashtag2 = "#hashtag2";
    final var expectedHashtags = List.of(expectedHashtag1, expectedHashtag2);
    final var actualHashtags = extractHashtags(
        new HandyUpdate()
            .setMessage(
                new HandyMessage()
                    .addMessageEntity(
                        new HandyMessageEntity()
                            .setType("hashtag")
                            .setText(expectedHashtag1)
                            .entity()
                    )
                    .addMessageEntity(
                        new HandyMessageEntity()
                            .setType("hashtag")
                            .setText(expectedHashtag2)
                            .entity()
                    )
                    .message()
            )
            .update()
    );
    assertThat(actualHashtags).isEqualTo(expectedHashtags);
  }

  @ParameterizedTest
  @MethodSource("candidatesWithAbsentMessageAndWithErrorMessage")
  void should_throw_exception_when_update_does_not_contain_message(Update update, String message) {
    assertThatThrownBy(() -> extractHashtags(update))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(message);
  }

  private static Stream<Arguments> candidatesWithAbsentMessageAndWithErrorMessage() {
    return Stream.of(
        Arguments.of(null, "update object can't be null"),
        Arguments.of(
            new HandyUpdate()
                .setMessage(null)
                .update(), "message object can't be null"
        )
    );
  }

  @Test
  void should_return_true_when_update_contains_text() {
    assertThat(messageTextExists(
        new HandyUpdate()
            .setMessage(
                new HandyMessage()
                    .setText("")
                    .message()
            )
            .update()
    )).isTrue();

  }

  @ParameterizedTest
  @MethodSource("candidatesWithAbsentTextAndWithoutErrorMessage")
  void should_return_false_when_update_does_not_contain_text(Update update) {
    assertThat(messageTextExists(update)).isFalse();
  }

  private static Stream<Arguments> candidatesWithAbsentTextAndWithoutErrorMessage() {
    return Stream.of(
        Arguments.of((Update) null),
        Arguments.of(
            new HandyUpdate()
                .setMessage(null)
                .update()
        ),
        Arguments.of(
            new HandyUpdate()
                .setMessage(
                    new HandyMessage()
                        .setText(null)
                        .message()
                )
                .update()
        )
    );
  }

  private static class HandyUpdate {

    private final Update update = new Update();

    HandyUpdate setMessage(Message message) {
      this.update.setMessage(message);
      return this;
    }

    Update update() {
      return this.update;
    }
  }

  private static class HandyMessage {

    private final Message message = new Message();
    private final List<MessageEntity> entities = new ArrayList<>();

    HandyMessage setText(String text) {
      this.message.setText(text);
      return this;
    }

    HandyMessage setChat(Chat chat) {
      this.message.setChat(chat);
      return this;
    }

    HandyMessage addMessageEntity(MessageEntity entity) {
      this.entities.add(entity);
      return this;
    }

    Message message() {
      this.message.setEntities(new ArrayList<>(this.entities));
      return this.message;
    }
  }

  private static class HandyChat {

    private final Chat chat = new Chat();

    HandyChat setId(Long id) {
      this.chat.setId(id);
      return this;
    }

    Chat chat() {
      return this.chat;
    }
  }

  private static class HandyMessageEntity {

    private final MessageEntity entity = new MessageEntity();

    HandyMessageEntity setType(String type) {
      this.entity.setType(type);
      return this;
    }

    HandyMessageEntity setText(String text) {
      this.entity.setText(text);
      return this;
    }

    MessageEntity entity() {
      return this.entity;
    }
  }
}
