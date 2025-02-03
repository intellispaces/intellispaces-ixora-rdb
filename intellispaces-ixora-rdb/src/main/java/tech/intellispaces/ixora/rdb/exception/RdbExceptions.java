package tech.intellispaces.ixora.rdb.exception;

import tech.intellispaces.commons.base.text.StringFunctions;

/**
 * Provider of the exception {@link RdbException}.
 */
public interface RdbExceptions {

  static RdbException withMessage(String message) {
    return new RdbException(message);
  }

  static RdbException withMessage(String template, Object... params) {
    return new RdbException(StringFunctions.resolveTemplate(template, params));
  }

  static RdbException withCauseAndMessage(Throwable cause, String message) {
    return new RdbException(message, cause);
  }

  static RdbException withCauseAndMessage(
      Throwable cause, String template, Object... params
  ) {
    return new RdbException(StringFunctions.resolveTemplate(template, params), cause);
  }
}
