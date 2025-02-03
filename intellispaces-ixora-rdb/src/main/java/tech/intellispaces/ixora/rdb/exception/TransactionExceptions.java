package tech.intellispaces.ixora.rdb.exception;

import tech.intellispaces.commons.base.text.StringFunctions;

/**
 * Provider of the exception {@link TransactionException}.
 */
public interface TransactionExceptions {

  static TransactionException withMessage(String message) {
    return new TransactionException(message);
  }

  static TransactionException withMessage(String template, Object... params) {
    return new TransactionException(StringFunctions.resolveTemplate(template, params));
  }

  static TransactionException withCauseAndMessage(Throwable cause, String message) {
    return new TransactionException(message, cause);
  }

  static TransactionException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new TransactionException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
