package tatbash.translation.yandex.translate;

import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Objects;

public record Response(TranslationText[] translations) {

  public Response(TranslationText[] translations) {
    this.translations = copyOf(requireNonNull(translations, "translations can't be null"), translations.length);
    if (Arrays.stream(this.translations).anyMatch(Objects::isNull)) {
      throw new NullPointerException("translations can't contain null elements");
    }
  }

  public TranslationText[] translations() {
    return copyOf(translations, translations.length);
  }

  public static record TranslationText(String text) {
    public TranslationText(String text) {
      this.text = requireNonNull(text, "text can't be null");
    }
  }
}
