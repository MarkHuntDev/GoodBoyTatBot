package tatbash.translation.yandex.translate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.config.YandexCloudTranslationProperties;
import tatbash.translation.utils.httpentity.HttpEntityBuilder;
import tatbash.translation.yandex.token.IamTokenKeeper;
import tatbash.translation.yandex.translate.Response.TranslationText;

@Slf4j
@RequiredArgsConstructor
@Service
public class YandexTranslateClient {

  @Qualifier("yandexTranslationRestTemplate")
  private final RestTemplate restTemplate;
  private final YandexCloudTranslationProperties properties;
  private final IamTokenKeeper tokenKeeper;

  /**
   * Translate text using Yandex Translate API.
   *
   * @param sourceLanguageCode source language
   * @param targetLanguageCode target language
   * @param texts              array of texts
   * @return array of translated texts
   */
  public String[] translate(String sourceLanguageCode, String targetLanguageCode, String[] texts) {
    final var httpEntity = buildHttpEntity(sourceLanguageCode, targetLanguageCode, texts);
    final var response = requestTranslations(httpEntity);
    final var translations = extractTranslations(response);
    return Arrays.stream(translations)
        .map(TranslationText::text)
        .toArray(String[]::new);
  }

  private HttpEntity<Object> buildHttpEntity(String sourceLanguageCode, String targetLanguageCode, String[] texts) {
    return HttpEntityBuilder
        .builder()
        .withBody(new Request(properties.folderId(), sourceLanguageCode, targetLanguageCode, texts))
        .withHeader("Authorization", List.of("Bearer " + tokenKeeper.getIamToken()))
        .build();
  }

  private ResponseEntity<Response> requestTranslations(HttpEntity<Object> httpEntity) {
    return this.restTemplate.exchange("/translate", HttpMethod.POST, httpEntity, Response.class);
  }

  private TranslationText[] extractTranslations(ResponseEntity<Response> response) {
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NullPointerException("response body can't be null"))
        .translations();
  }
}
