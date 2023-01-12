package tatbash.translation.yandex.translate.error;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import tatbash.infrastructure.exception.YandexTranslateException;

public class YandexTranslateErrorHandler implements ResponseErrorHandler {
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().isError();
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    if (response.getStatusCode().isError()) {
      try (final var reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
        final var body = reader.lines().collect(joining(""));
        final var mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final var error = mapper.readValue(body, YandexTranslateErrorResponse.class);
        throw new YandexTranslateException(error.message());
      }
    }
  }
}
