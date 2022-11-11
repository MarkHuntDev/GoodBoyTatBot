package tatbash.translation.yandex.token;

import static java.util.Objects.requireNonNull;

public record Request(String yandexPassportOauthToken) {
  public Request(String yandexPassportOauthToken) {
    this.yandexPassportOauthToken = requireNonNull(yandexPassportOauthToken, "yandexPassportOauthToken can't be null");
  }
}
