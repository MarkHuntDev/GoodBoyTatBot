package tatbash.translation.yandex.token;

import static java.util.Objects.requireNonNull;

import java.time.OffsetDateTime;

public record Response(String iamToken, OffsetDateTime expiresAt) {
  public Response(String iamToken, OffsetDateTime expiresAt) {
    this.iamToken = requireNonNull(iamToken, "iamToken can't be null");
    this.expiresAt = requireNonNull(expiresAt, "expiresAt can't be null");
  }
}
