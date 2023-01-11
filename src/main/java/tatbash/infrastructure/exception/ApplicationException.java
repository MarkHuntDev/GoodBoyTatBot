package tatbash.infrastructure.exception;

public class ApplicationException extends RuntimeException {
  public ApplicationException(String message) {
    super(message);
  }
}
