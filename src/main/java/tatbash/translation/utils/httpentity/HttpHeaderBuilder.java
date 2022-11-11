package tatbash.translation.utils.httpentity;

import java.util.List;

public interface HttpHeaderBuilder<T> {
  HttpEntityBuilder<T> withHeader(String name, List<String> values);
}
