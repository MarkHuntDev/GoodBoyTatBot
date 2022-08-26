package tatbash.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class ApplicationPropertiesTest {

  @Test
  void should_throw_exception_when_param_is_null() {
    assertThatThrownBy(() -> new ApplicationProperties(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("markers can't be null");
  }

  @Test
  void should_create_an_instance_successfully() {
    assertThat(new ApplicationProperties(Collections.emptySet()).markers())
        .isEmpty();
  }
}
