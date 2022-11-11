package tatbash.translation.yandex.translate;

import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;

public record Request(String folderId, String sourceLanguageCode, String targetLanguageCode, String[] texts) {

  public Request(String folderId, String sourceLanguageCode, String targetLanguageCode, String[] texts) {
    this.folderId = requireNonNull(folderId, "folderId can't be null");
    this.sourceLanguageCode = requireNonNull(sourceLanguageCode, "sourceLanguageCode can't be null");
    this.targetLanguageCode = requireNonNull(targetLanguageCode, "targetLanguageCode can't be null");
    this.texts = copyOf(requireNonNull(texts, "texts can't be null"), texts.length);
  }

  public String[] texts() {
    return copyOf(texts, texts.length);
  }
}
