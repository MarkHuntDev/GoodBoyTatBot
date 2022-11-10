package tatbash.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tatbash.telegram.UpdateUtils.extractChatId;
import static tatbash.telegram.UpdateUtils.extractHashtags;
import static tatbash.telegram.UpdateUtils.extractText;
import static tatbash.telegram.UpdateUtils.messageTextExists;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Update;
import tatbash.telegram.FixtureUtils.ChatBuilder;
import tatbash.telegram.FixtureUtils.MessageBuilder;
import tatbash.telegram.FixtureUtils.MessageEntityBuilder;
import tatbash.telegram.FixtureUtils.UpdateBuilder;

class UpdateUtilsTest {

  @Test
  void should_return_extracted_chat_id() {
    // given:
    final var expectedChatId = 999L;
    final var expectedUpdate = new UpdateBuilder()
        .setMessage(
            new MessageBuilder()
                .setChat(
                    new ChatBuilder()
                        .setId(expectedChatId)
                        .build()
                )
                .build()
        )
        .build();

    // when:
    final var actualChatId = extractChatId(expectedUpdate);

    // then:
    assertThat(actualChatId)
        .isEqualTo(expectedChatId);
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
        Arguments.of(
            null,
            "update object can't be null"
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(null)
                .build(),
            "message object can't be null"
        )
    );
  }

  @Test
  void should_return_extracted_text() {
    // given:
    final var expectedText = "test text";
    final var expectedUpdate = new UpdateBuilder()
        .setMessage(
            new MessageBuilder()
                .setText(expectedText)
                .build()
        )
        .build();

    // when:
    final var actualChatId = extractText(expectedUpdate);

    // then:
    assertThat(actualChatId)
        .isEqualTo(expectedText);
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
        Arguments.of(
            null,
            "update object can't be null"
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(null)
                .build(),
            "message object can't be null"
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(
                    new MessageBuilder()
                        .setText(null)
                        .build()
                )
                .build(),
            "message text can't be null"
        )
    );
  }

  @Test
  void should_return_extracted_hashtags() {
    // given:
    final var expectedHashtag1 = "#hashtag1";
    final var expectedHashtag2 = "#hashtag2";
    final var expectedHashtags = Set.of(expectedHashtag1, expectedHashtag2);
    final var expectedEntityMessage1 = new MessageEntityBuilder()
        .setType("hashtag")
        .setText(expectedHashtag1)
        .build();
    final var expectedEntityMessage2 = new MessageEntityBuilder()
        .setType("hashtag")
        .setText(expectedHashtag2)
        .build();
    final var expectedUpdate = new UpdateBuilder()
        .setMessage(
            new MessageBuilder()
                .addMessageEntity(expectedEntityMessage1)
                .addMessageEntity(expectedEntityMessage2)
                .build()
        )
        .build();

    // when:
    final var actualHashtags = extractHashtags(expectedUpdate);

    // then:
    assertThat(actualHashtags)
        .isEqualTo(expectedHashtags);
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
        Arguments.of(
            null,
            "update object can't be null"
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(null)
                .build(),
            "message object can't be null"
        )
    );
  }

  @Test
  void should_return_true_when_update_contains_text() {
    // given:
    final var expectedUpdate = new UpdateBuilder()
        .setMessage(
            new MessageBuilder()
                .setText("")
                .build()
        )
        .build();

    // when:
    final var exists = messageTextExists(expectedUpdate);

    // then:
    assertThat(exists)
        .isTrue();

  }

  @ParameterizedTest
  @MethodSource("candidatesWithAbsentTextAndWithoutErrorMessage")
  void should_return_false_when_update_does_not_contain_text(Update update) {
    // when:
    final var exists = messageTextExists(update);

    // then:
    assertThat(exists)
        .isFalse();
  }

  private static Stream<Arguments> candidatesWithAbsentTextAndWithoutErrorMessage() {
    return Stream.of(
        Arguments.of(
            (Update) null
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(null)
                .build()
        ),
        Arguments.of(
            new UpdateBuilder()
                .setMessage(
                    new MessageBuilder()
                        .setText(null)
                        .build()
                )
                .build()
        )
    );
  }
}
