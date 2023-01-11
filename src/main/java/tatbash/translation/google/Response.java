package tatbash.translation.google;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;

@JsonDeserialize(using = ResponseDeserializer.class)
public record Response(String translated) {
  public Response {
    if (StringUtils.isBlank(translated)) {
      throw new IllegalArgumentException("translated can't be null or empty");
    }
  }
}
