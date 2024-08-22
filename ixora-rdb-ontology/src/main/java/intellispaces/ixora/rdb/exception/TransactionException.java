package intellispaces.ixora.rdb.exception;

import tech.intellispaces.core.exception.TraverseException;

public class TransactionException extends TraverseException {

  public TransactionException(String messageTemplate, Object... messageParams) {
    super(messageTemplate, messageParams);
  }

  public TransactionException(Throwable cause, String messageTemplate, Object... messageParams) {
    super(cause, messageTemplate, messageParams);
  }

  public static TransactionException withMessage(String messageTemplate, Object... messageParams) {
    return new TransactionException(null, messageTemplate, messageParams);
  }

  public static TransactionException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new TransactionException(cause, messageTemplate, messageParams);
  }
}
