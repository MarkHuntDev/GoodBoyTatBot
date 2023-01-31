package tatbash.translation.google;

import static tatbash.translation.utils.Utils.escapeQuotes;
import static tatbash.translation.utils.Utils.quote;
import static tatbash.translation.utils.Utils.unescapeQuotes;
import static tatbash.translation.utils.Utils.unquote;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleTranslateClient {

  @Qualifier("googleTranslationRestTemplate")
  private final RestTemplate restTemplate;

  public String translate(String sourceLanguageCode, String targetLanguageCode, String text) {
    final var quoted = prepare(text);
    final var params = params(sourceLanguageCode, targetLanguageCode, quoted);
    final var response = requestTranslation(params);
    final var translated = extractTranslation(response);
    return refine(translated);
  }

  private String prepare(String raw) {
    return quote(escapeQuotes(raw.trim()));
  }

  private String refine(String polluted) {
    return unquote(unescapeQuotes(polluted.trim()));
  }

  // todo: define params through HttpEntityBuilder
  private MultiValueMap<String, String> params(String sourceLanguageCode, String targetLanguageCode, String text) {
    final var params = new LinkedMultiValueMap<String, String>();
    params.add("client", "gtx");
    params.add("sl", sourceLanguageCode);
    params.add("tl", targetLanguageCode);
    params.add("dt", "t");
    params.add("ie", "UTF-8");
    params.add("oe", "UTF-8");
    params.add("otf", "1");
    params.add("ssel", "0");
    params.add("tsel", "0");
    params.add("kc", "7");
    params.add("dt", "at");
    params.add("dt", "bd");
    params.add("dt", "ex");
    params.add("dt", "ld");
    params.add("dt", "md");
    params.add("dt", "qca");
    params.add("dt", "rw");
    params.add("dt", "rm");
    params.add("dt", "ss");
    params.add("q", text);
    return params;
  }

  private ResponseEntity<Response> requestTranslation(MultiValueMap<String, String> params) {
    return restTemplate.postForEntity("/translate_a/single",
        new HttpEntity<>(params, new HttpHeaders()), Response.class);
  }

  private String extractTranslation(ResponseEntity<Response> response) {
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NullPointerException("response body can't be null"))
        .translated();
  }
}
