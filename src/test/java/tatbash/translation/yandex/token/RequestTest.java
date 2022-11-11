package tatbash.translation.yandex.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestTest {

  @ParameterizedTest
  @MethodSource("nullParamsCandidates")
  void should_throw_exception_when_any_param_is_null(String yandexPassportOauthToken, String exceptionMessage) {
    assertThatThrownBy(
        () -> new Request(yandexPassportOauthToken))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(exceptionMessage);
  }

  private static Stream<Arguments> nullParamsCandidates() {
    return Stream.of(
        Arguments.of(null, "yandexPassportOauthToken can't be null")
    );
  }

  @Test
  void should_create_an_instance_successfully() {
    final var actual = new Request("oauth-token");
    assertThat(actual.yandexPassportOauthToken())
        .isEqualTo("oauth-token");
  }
}