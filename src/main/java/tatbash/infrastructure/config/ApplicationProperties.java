package tatbash.infrastructure.config;

import static java.util.Collections.unmodifiableSet;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.util.CollectionUtils;

@ConstructorBinding
@ConfigurationProperties("app")
public record ApplicationProperties(Set<String> markers) {

  public ApplicationProperties(Set<String> markers) {
    if (CollectionUtils.isEmpty(markers)) {
      throw new IllegalArgumentException("markers can't be null or empty");
    }
    this.markers = unmodifiableSet(markers);
  }

  @Override
  public Set<String> markers() {
    return unmodifiableSet(this.markers);
  }
}
