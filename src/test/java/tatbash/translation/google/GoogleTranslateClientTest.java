package tatbash.translation.google;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith({MockitoExtension.class})
class GoogleTranslateClientTest {

  @Mock
  private RestTemplate restTemplate;
  @InjectMocks
  private GoogleTranslateClient translateClient;

  @Test
  void should_throw_exception_when_response_body_is_null() {
    // given:
    when(restTemplate.postForEntity(anyString(), any(), any()))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
    // then:
    assertThatThrownBy(() -> translateClient.translate("", "", ""))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("response body can't be null");
    verify(restTemplate)
        .postForEntity(anyString(), any(), any());
  }
}
