package intellispaces.ixora.rdb.exception;

import intellispaces.core.exception.TraverseException;

public class RdbException extends TraverseException {

  public RdbException(String messageTemplate, Object... messageParams) {
    super(messageTemplate, messageParams);
  }

  public RdbException(Throwable cause, String messageTemplate, Object... messageParams) {
    super(cause, messageTemplate, messageParams);
  }

  public static RdbException withMessage(String messageTemplate, Object... messageParams) {
    return new RdbException(null, messageTemplate, messageParams);
  }

  public static RdbException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new RdbException(cause, messageTemplate, messageParams);
  }
}
