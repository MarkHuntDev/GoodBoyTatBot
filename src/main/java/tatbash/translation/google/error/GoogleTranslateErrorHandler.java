package tatbash.translation.google.error;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import tatbash.infrastructure.exception.GoogleTranslateException;

public class GoogleTranslateErrorHandler implements ResponseErrorHandler {
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().isError();
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    if (response.getStatusCode().isError()) {
      try (final var reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
        final var body = reader.lines().collect(joining(""));
        final var errorMessage = """
            Google API error:
            HTTP Status: %d
            Response body:
            %s
            """.formatted(response.getRawStatusCode(), body);
        throw new GoogleTranslateException(errorMessage);
      }
    }
  }
}
