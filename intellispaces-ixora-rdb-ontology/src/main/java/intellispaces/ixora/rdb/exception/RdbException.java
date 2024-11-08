package intellispaces.ixora.rdb.exception;

import intellispaces.jaquarius.exception.TraverseException;

public class RdbException extends TraverseException {

  public RdbException(String message) {
    super(message);
  }

  public RdbException(String message, Throwable cause) {
    super(message, cause);
  }
}
