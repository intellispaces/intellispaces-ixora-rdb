package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import intellispaces.ixora.rdb.TransactionHandle;
import tech.intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispacesframework.commons.function.ThrowingConsumer;

import java.util.function.Consumer;

public class TransactionFunctions {

  public static void transactional(
      TransactionFactoryHandle transactionFactory, Consumer<TransactionHandle> operation
  ) {
    TransactionHandle tx = null;
    try {
      tx = transactionFactory.getTransaction();
      operation.accept(tx);
      commit(tx);
    } catch (TransactionException e) {
      throw e;
    } catch (RuntimeException e) {
      rollback(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Runtime exception {} occurred while transaction was executed. Transaction has been rolled back",
          e.getClass().getSimpleName());
    }
  }

  public static <E extends Throwable> void transactional(
      TransactionFactoryHandle transactionFactory, ThrowingConsumer<TransactionHandle, E> operation
  ) {
    TransactionHandle tx = null;
    try {
      tx = transactionFactory.getTransaction();
      operation.accept(tx);
      tx.commit();
    } catch (TransactionException e) {
      throw e;
    } catch (RuntimeException e) {
      rollback(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Runtime exception {} occurred while transaction was executed. Transaction has been rolled back",
          e.getClass().getSimpleName());
    } catch (Throwable e) {
      commit(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Checked exception {} occurred while transaction was executed. Transaction has been committed",
          e.getClass().getSimpleName());
    }
  }

  public static void commit(TransactionHandle tx) {
    commit(tx, null);
  }

  public static void commit(TransactionHandle tx, Throwable reason) {
    try {
      tx.commit();
    } catch (Throwable e) {
      if (reason != null) {
        TransactionException te = TransactionException.withCauseAndMessage(e, "Failed to commit transaction after exception {}",
            reason.getClass().getCanonicalName());
        te.addSuppressed(reason);
        throw te;
      }
      throw TransactionException.withCauseAndMessage(e, "Failed to commit transaction");
    }
  }

  public static void rollback(TransactionHandle tx) {
    rollback(tx, null);
  }

  public static void rollback(TransactionHandle tx, Throwable reason) {
    try {
      tx.rollback();
    } catch (Throwable e) {
      if (reason != null) {
        TransactionException te = TransactionException.withCauseAndMessage(e, "Failed to roll back transaction after exception {}",
            reason.getClass().getCanonicalName());
        te.addSuppressed(reason);
        throw te;
      }
      throw TransactionException.withCauseAndMessage(e, "Failed to roll back transaction");
    }
  }

  private TransactionFunctions() {}
}
