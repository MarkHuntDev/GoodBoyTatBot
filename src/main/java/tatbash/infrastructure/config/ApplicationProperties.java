package tatbash.infrastructure.config;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app")
public record ApplicationProperties(Set<String> markers) {

  public ApplicationProperties(Set<String> markers) {
    this.markers = unmodifiableSet(requireNonNull(markers, "markers can't be null"));
  }

  @Override
  public Set<String> markers() {
    return unmodifiableSet(this.markers);
  }
}
