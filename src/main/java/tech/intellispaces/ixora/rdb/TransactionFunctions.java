package tech.intellispaces.ixora.rdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.framework.commons.exception.CoveredCheckedException;
import tech.intellispaces.framework.commons.function.ThrowingConsumer;

import java.util.function.Consumer;

/**
 * Transaction functions.
 */
public class TransactionFunctions {

  private static final Logger LOG = LoggerFactory.getLogger(TransactionFunctions.class);

  public static void transactional(TransactionFactory transactionFactory, Consumer<Transaction> operation) {
    Transaction tx = null;
    try {
      tx = transactionFactory.getTransaction();
      operation.accept(tx);
      commit(tx);
    } catch (TransactionException e) {
      throw e;
    } catch (RuntimeException | Error e) {
      rollback(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Runtime exception {} occurred while transaction was executed. " +
              "Transaction has been rolled back",
          e.getClass().getSimpleName());
    }
  }

  public static <E extends Throwable> void transactional(
      TransactionFactory transactionFactory, ThrowingConsumer<Transaction, E> operation
  ) {
    Transaction tx = null;
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

  public static void commit(Transaction tx) {
    commit(tx, null);
  }

  public static void commit(Transaction tx, Throwable reason) {
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

  public static void rollback(Transaction tx) {
    rollback(tx, null);
  }

  public static void rollback(Transaction tx, Throwable reason) {
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
