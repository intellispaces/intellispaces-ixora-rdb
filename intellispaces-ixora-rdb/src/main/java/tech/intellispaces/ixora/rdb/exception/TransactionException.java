package tech.intellispaces.ixora.rdb.exception;

public class TransactionException extends RdbException {

  public TransactionException(String message) {
    super(message);
  }

  public TransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
