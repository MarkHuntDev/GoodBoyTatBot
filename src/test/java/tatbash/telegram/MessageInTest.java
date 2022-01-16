package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
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
    assertThat(whenUpdateIsNull.exists()).isFalse();
    assertThat(whenUpdateIsNull.text()).isNull();

    assertThat(whenMessageIsNull.exists()).isFalse();
    assertThat(whenMessageIsNull.text()).isNull();

    assertThat(whenMessageTextIsNull.exists()).isFalse();
    assertThat(whenMessageTextIsNull.text()).isNull();
  }

  @Test
  void hasMessage_should_return_true_when_update_contains_message_text() {
    // when:
    final var messageWithNotNullText = MessageIn
        .builder()
        .from(messageWithNonBlankText())
        .build();

    // then:
    assertThat(messageWithNotNullText.exists()).isTrue();
    assertThat(messageWithNotNullText.text()).isNotNull();
    assertThat(messageWithNotNullText.text()).isEmpty();
  }

  @Test
  void should_return_markers_with_hashtag_type() {
    // when:
    final var messageWithMarkers = MessageIn
        .builder()
        .from(messageWithNonBlankTextAndExistentMarker())
        .build();

    // then:
    assertThat(messageWithMarkers.markers()).containsExactly("#marker");
  }

  private Update messageWithNullText() {
    final var update = new Update();
    update.setMessage(new Message());
    return update;
  }

  private Update messageWithNonBlankText() {
    final var update = new Update();
    final var message = new Message();
    message.setText("");
    update.setMessage(message);
    return update;
  }

  private Update messageWithNonBlankTextAndExistentMarker() {
    final var update = messageWithNonBlankText();
    update.getMessage().setText("@SomeUser#marker");
    final var markerWithNullType = new MessageEntity();
    markerWithNullType.setOffset(0);
    markerWithNullType.setLength(9);
    markerWithNullType.setType("mention");
    markerWithNullType.setText("@SomeUser");
    final var markerWithHashtagType = new MessageEntity();
    markerWithHashtagType.setOffset(9);
    markerWithHashtagType.setLength(7);
    markerWithHashtagType.setType("hashtag");
    markerWithHashtagType.setText("#marker");
    update.getMessage().setEntities(List.of(markerWithNullType, markerWithHashtagType));
    return update;
  }
}