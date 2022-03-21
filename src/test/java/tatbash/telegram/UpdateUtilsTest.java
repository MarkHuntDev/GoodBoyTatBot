package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.messageTextExists;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
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
  @MethodSource("candidatesWithAbsentText")
  void should_return_false_when_update_does_not_contain_text(Update update) {
    assertThat(messageTextExists(update)).isFalse();
  }

  private static Stream<Arguments> candidatesWithAbsentText() {
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

    HandyMessage setText(String text) {
      this.message.setText(text);
      return this;
    }

    HandyMessage setChat(Chat chat) {
      this.message.setChat(chat);
      return this;
    }

    Message message() {
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
}
