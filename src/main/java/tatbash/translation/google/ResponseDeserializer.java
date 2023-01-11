package tatbash.translation.google;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import tatbash.infrastructure.exception.GoogleTranslateException;

public class ResponseDeserializer extends StdDeserializer<Response> {

  @SuppressWarnings("unused") // used by framework
  public ResponseDeserializer() {
    this(null);
  }

  protected ResponseDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Response deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final var rootArray = parser.getCodec().readTree(parser);
    if (!rootArray.isArray()) {
      throw new GoogleTranslateException("rootNode is not an array");
    }
    final var firstArray = ((ArrayNode) rootArray).get(0);
    if (!firstArray.isArray()) {
      throw new GoogleTranslateException("firstNode is not an array");
    }
    final var secondArray = firstArray.get(0);
    if (!secondArray.isArray()) {
      throw new GoogleTranslateException("secondNode is not an array");
    }
    final var translated = secondArray.get(0).asText();
    return new Response(translated);
  }
}
