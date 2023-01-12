package tatbash.translation.google.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import tatbash.infrastructure.exception.GoogleTranslateException;

class GoogleTranslateErrorHandlerTest {

  @Test
  void should_has_error() throws IOException {
    assertThat(
        new GoogleTranslateErrorHandler()
            .hasError(new MockClientHttpResponse("".getBytes(StandardCharsets.UTF_8), HttpStatus.BAD_REQUEST))
    ).isTrue();
  }

  @Test
  void should_has_not_error() throws IOException {
    assertThat(
        new GoogleTranslateErrorHandler()
            .hasError(new MockClientHttpResponse("".getBytes(StandardCharsets.UTF_8), HttpStatus.OK))
    ).isFalse();
  }

  @Test
  void should_throw_exception() {
    assertThatThrownBy(
        () -> new GoogleTranslateErrorHandler()
            .handleError(
                new MockClientHttpResponse("""
                    Внутренняя ошибка сервера.
                    """.getBytes(StandardCharsets.UTF_8),
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
    )
        .isInstanceOf(GoogleTranslateException.class)
        .hasMessage("""
            Google API error:
            HTTP Status: 500
            Response body:
            Внутренняя ошибка сервера.
            """);
  }

  @Test
  void should_not_throw_exception() throws IOException {
    new GoogleTranslateErrorHandler()
        .handleError(
            new MockClientHttpResponse("""
                Успешный ответ.
                """.getBytes(StandardCharsets.UTF_8),
                HttpStatus.OK
            )
        );
  }
}