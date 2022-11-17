package tatbash.infrastructure.config;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app")
public record ApplicationProperties(List<LanguagePairProperty> hashtags) {

  public ApplicationProperties(List<LanguagePairProperty> hashtags) {
    this.hashtags = unmodifiableList(requireNonNull(hashtags, "hashtags can't be null"));
  }

  public List<LanguagePairProperty> hashtags() {
    return unmodifiableList(this.hashtags);
  }

  public record LanguagePairProperty(String hashtag, String sourceLanguage, String targetLanguage) {
    public LanguagePairProperty {
      if (StringUtils.isBlank(hashtag)) {
        throw new IllegalArgumentException("hashtag can't be null or empty");
      }
      if (StringUtils.isBlank(sourceLanguage)) {
        throw new IllegalArgumentException("sourceLanguage can't be null or empty");
      }
      if (StringUtils.isBlank(targetLanguage)) {
        throw new IllegalArgumentException("targetLanguage can't be null or empty");
      }
    }
  }
}
