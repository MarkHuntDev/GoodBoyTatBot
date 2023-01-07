package tatbash.infrastructure.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import tatbash.infrastructure.exception.YandexTranslateException;

class YandexTranslateErrorHandlerTest {
  @Test
  void should_has_error() throws IOException {
    assertThat(
        new YandexTranslateErrorHandler()
            .hasError(new MockClientHttpResponse("".getBytes(StandardCharsets.UTF_8), HttpStatus.BAD_REQUEST))
    ).isTrue();
  }

  @Test
  void should_has_not_error() throws IOException {
    assertThat(
        new YandexTranslateErrorHandler()
            .hasError(new MockClientHttpResponse("".getBytes(StandardCharsets.UTF_8), HttpStatus.OK))
    ).isFalse();
  }

  @Test
  void should_throw_exception() {
    assertThatThrownBy(
        () -> new YandexTranslateErrorHandler()
            .handleError(
                new MockClientHttpResponse(
                    """
                        {
                           "code": 1,
                           "message": "Внутренняя ошибка сервера.",
                           "details": [
                             {
                               "@type": "type.googleapis.com/google.rpc.RequestInfo",
                               "requestId": "00000000-0000-0000-0000-000000000001"
                             }
                           ]
                         }
                        """.getBytes(StandardCharsets.UTF_8),
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
    )
        .isInstanceOf(YandexTranslateException.class)
        .hasMessage("Внутренняя ошибка сервера.");
  }

  @Test
  void should_not_throw_exception() throws IOException {
    new YandexTranslateErrorHandler()
        .handleError(
            new MockClientHttpResponse(
                """
                    {
                       "code": 1,
                       "message": "Успешный запрос.",
                       "details": [
                         {
                           "@type": "type.googleapis.com/google.rpc.RequestInfo",
                           "requestId": "00000000-0000-0000-0000-000000000001"
                         }
                       ]
                     }
                    """.getBytes(StandardCharsets.UTF_8),
                HttpStatus.OK
            )
        );
  }
}