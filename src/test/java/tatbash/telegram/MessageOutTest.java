package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageOutTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_any_param_is_null(Long chatId,
                                                     String text,
                                                     String exceptionMessage) {
    assertThatThrownBy(() -> new MessageOut(chatId, text))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(null, null, "chatId can't be null"),
        Arguments.of(1L, null, "text can't be null")
    );
  }

  @ParameterizedTest
  @MethodSource("messageParamsCandidates")
  void should_return_true_when_message_is_not_blank_and_false_when_it_is_blank(String text, boolean exists) {
    assertThat(new MessageOut(1L, text).exists())
        .isEqualTo(exists);
  }

  private static Stream<Arguments> messageParamsCandidates() {
    return Stream.of(
        Arguments.of("some text", true),
        Arguments.of("", false),
        Arguments.of(" ", false),
        Arguments.of("  ", false)
    );
  }

  @Test
  void should_successfully_create_send_message_object_from_message_out() {
    final var sendMessage = new MessageOut(1L, "Message!").toSendMessage();
    assertThat(sendMessage.getChatId())
        .isEqualTo("1");
    assertThat(sendMessage.getText())
        .isEqualTo("Message!");
  }

  @Test
  void should_always_return_message_out_with_empty_message() {
    assertThat(MessageOut.empty(1L).exists())
        .isFalse();
  }
}
