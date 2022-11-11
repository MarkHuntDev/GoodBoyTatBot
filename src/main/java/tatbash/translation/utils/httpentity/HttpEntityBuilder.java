package tatbash.translation.utils.httpentity;

import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpEntityBuilder<T> implements HttpBodyBuilder<T>, HttpHeaderBuilder<T> {

  private T body;
  private final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

  public static <T> HttpBodyBuilder<T> builder() {
    return new HttpEntityBuilder<>();
  }

  @Override
  public HttpEntityBuilder<T> withBody(T body) {
    this.body = body;
    return this;
  }

  @Override
  public HttpEntityBuilder<T> withHeader(String name, List<String> values) {
    this.headers.put(name, values);
    return this;
  }

  public HttpEntity<T> build() {
    return new HttpEntity<>(this.body, this.headers);
  }
}
