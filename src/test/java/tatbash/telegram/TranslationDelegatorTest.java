package tatbash.telegram;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tatbash.telegram.FixtureUtils.ChatBuilder;
import tatbash.telegram.FixtureUtils.MessageBuilder;
import tatbash.telegram.FixtureUtils.UpdateBuilder;
import tatbash.translation.TranslationService;

@ExtendWith({MockitoExtension.class})
class TranslationDelegatorTest {

  @Mock
  private TranslationService service;

  @InjectMocks
  private TranslationDelegator delegator;

  @Test
  void should_not_call_service_when_text_message_does_not_exist() {
    // given:
    final var executor = Mockito.mock(TelegramBotExecutor.class);
    // when:
    delegator.translate(
        new UpdateBuilder()
            .setMessage(
                new MessageBuilder()
                    .setText(null)
                    .build()
            )
            .build(),
        executor
    );
    // then:
    verifyNoInteractions(service, executor);
  }

  @Test
  void should_call_service_but_not_executor_when_text_message_exists_but_response_does_not() {
    // given:
    final var executor = Mockito.mock(TelegramBotExecutor.class);
    // given:
    when(service.translate(any(MessageIn.class)))
        .thenReturn(MessageOut.empty(1L));
    // when:
    delegator.translate(
        new UpdateBuilder()
            .setMessage(
                new MessageBuilder()
                    .setChat(
                        new ChatBuilder()
                            .setId(1L)
                            .build()
                    )
                    .setText("test")
                    .build()
            )
            .build(),
        executor
    );
    // then:
    verify(service).translate(any(MessageIn.class));
    verifyNoInteractions(executor);
  }

  @Test
  void should_call_service_and_executor_when_text_message_exists_and_response_does() {
    // given:
    final var executor = Mockito.mock(TelegramBotExecutor.class);
    // given:
    when(service.translate(any(MessageIn.class)))
        .thenReturn(new MessageOut(1L, "test"));
    // when:
    delegator.translate(
        new UpdateBuilder()
            .setMessage(
                new MessageBuilder()
                    .setChat(
                        new ChatBuilder()
                            .setId(1L)
                            .build()
                    )
                    .setText("test")
                    .build()
            )
            .build(),
        executor
    );
    // then:
    verify(service).translate(any(MessageIn.class));
    verify(executor).sendMessage(any(SendMessage.class));
  }
}
