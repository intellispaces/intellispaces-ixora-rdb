package tech.intellispaces.ixora.rdb.exception;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

public class TransactionException extends UnexpectedViolationException {

  public TransactionException(String messageTemplate, Object... messageParams) {
    super(messageTemplate, messageParams);
  }

  public TransactionException(Throwable cause, String messageTemplate, Object... messageParams) {
    super(cause, messageTemplate, messageParams);
  }

  public static TransactionException withMessage(String messageTemplate, Object... messageParams) {
    return new TransactionException((Throwable)null, messageTemplate, messageParams);
  }

  public static TransactionException withCauseAndMessage(Throwable cause, String messageTemplate, Object... messageParams) {
    return new TransactionException(cause, messageTemplate, messageParams);
  }
}
