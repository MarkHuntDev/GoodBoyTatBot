package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class MessageInTest {

  @Test
  void hasMessage_should_return_false_when_update_does_not_contain_message_text() {
    // when:
    final var whenUpdateIsNull = MessageIn
        .builder()
        .from(null)
        .build();

    final var whenMessageIsNull = MessageIn
        .builder()
        .from(new Update())
        .build();

    final var whenMessageTextIsNull = MessageIn
        .builder()
        .from(messageWithNullText())
        .build();

    // then:
    assertThat(whenUpdateIsNull.hasMessage()).isFalse();
    assertThat(whenUpdateIsNull.text()).isNull();

    assertThat(whenMessageIsNull.hasMessage()).isFalse();
    assertThat(whenMessageIsNull.text()).isNull();

    assertThat(whenMessageTextIsNull.hasMessage()).isFalse();
    assertThat(whenMessageTextIsNull.text()).isNull();
  }

  @Test
  void hasMessage_should_return_true_when_update_contains_message_text() {
    // when:
    final var messageWithNotNullText = MessageIn
        .builder()
        .from(messageWithNotNullText())
        .build();

    // then:
    assertThat(messageWithNotNullText.hasMessage()).isTrue();
    assertThat(messageWithNotNullText.text()).isNotNull();
    assertThat(messageWithNotNullText.text()).isEmpty();
  }

  private Update messageWithNullText() {
    final var update = new Update();
    update.setMessage(new Message());
    return update;
  }

  private Update messageWithNotNullText() {
    final var update = new Update();
    final var message = new Message();
    message.setText("");
    update.setMessage(message);
    return update;
  }
}