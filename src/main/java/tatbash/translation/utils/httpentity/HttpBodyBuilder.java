package tatbash.translation.utils.httpentity;

public interface HttpBodyBuilder<T> {
  HttpEntityBuilder<T> withBody(T body);
}
