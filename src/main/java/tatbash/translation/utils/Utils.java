package tatbash.translation.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

  public static String quote(String unquoted) {
    return "\"" + unquoted + "\"";
  }

  public static String unquote(String quoted) {
    String unquoted = quoted;
    if (unquoted.startsWith("\"")) {
      unquoted = unquoted.substring(1);
    }
    if (unquoted.endsWith("\"")) {
      unquoted = unquoted.substring(0, unquoted.length() - 1);
    }
    return unquoted;
  }

  public static String escapeQuotes(String raw) {
    return raw.replace("\"", "\\\"");
  }

  public static String unescapeQuotes(String polluted) {
    return polluted.replace("\\\"", "\"");
  }
}
