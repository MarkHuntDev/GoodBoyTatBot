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

  @Test
  void should_create_an_instance_successfully() {
    final var actual = MessageOut.of(
        new FixtureUtils.UpdateBuilder()
            .setMessage(
                new FixtureUtils.MessageBuilder()
                    .setChat(
                        new FixtureUtils.ChatBuilder()
                            .setId(1L)
                            .build()
                    )
                    .setText("@UserExample#HashtagExample")
                    .addMessageEntity(
                        new FixtureUtils.MessageEntityBuilder()
                            .setType("mention")
                            .setText("@UserExample")
                            .setOffset(0)
                            .setLength(12)
                            .build()
                    )
                    .addMessageEntity(
                        new FixtureUtils.MessageEntityBuilder()
                            .setType("hashtag")
                            .setText("#HashtagExample")
                            .setOffset(12)
                            .setLength(15)
                            .build()
                    )
                    .build()
            )
            .build()
    );
    assertThat(actual.chatId())
        .isEqualTo(1L);
    assertThat(actual.text())
        .isEqualTo("@UserExample#HashtagExample");
  }
}