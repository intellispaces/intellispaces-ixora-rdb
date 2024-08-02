package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import intellispaces.ixora.rdb.TransactionHandle;
import intellispaces.ixora.rdb.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.CoveredCheckedException;
import tech.intellispaces.commons.function.ThrowableConsumer;

/**
 * Transaction functions.
 */
public class TransactionFunctions {

  private static final Logger LOG = LoggerFactory.getLogger(TransactionFunctions.class);

  public static <E extends Throwable> void transactional(
      TransactionFactoryHandle transactionFactory, ThrowableConsumer<TransactionHandle, E> operation
  ) {
    TransactionHandle tx = null;
    try {
      tx = transactionFactory.getTransaction();
      operation.accept(tx);
      tx.commit();
    } catch (TransactionException e) {
      throw e;
    } catch (RuntimeException | Error e) {
      rollback(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Runtime exception {} occurred while transaction was executed. " +
              "Transaction has been rolled back",
          e.getClass().getSimpleName());
    } catch (Throwable e) {
      LOG.info("Checked exception " + e.getClass().getCanonicalName() + " occurred while transaction was executed. " +
          "Transaction will be committed");
      commit(tx, e);
      throw CoveredCheckedException.withCause(e);
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
