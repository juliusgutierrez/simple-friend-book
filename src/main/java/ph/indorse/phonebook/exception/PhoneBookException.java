package ph.indorse.phonebook.exception;

public class PhoneBookException extends RuntimeException {

  public PhoneBookException() {
  }

  public PhoneBookException(String message) {
    super(message);
  }

  public PhoneBookException(String message, Throwable cause) {
    super(message, cause);
  }
}
