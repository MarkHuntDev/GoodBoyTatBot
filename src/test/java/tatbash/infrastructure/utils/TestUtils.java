package tatbash.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

  @SneakyThrows({IOException.class})
  public static String readString(String filename) {
    return new String(Files.readAllBytes(Paths.get("src/test/resources/json/" + filename)));
  }
}
