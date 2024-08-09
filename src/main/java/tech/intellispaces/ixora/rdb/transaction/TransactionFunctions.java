package tech.intellispaces.ixora.rdb.transaction;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import intellispaces.ixora.rdb.TransactionHandle;
import intellispaces.ixora.rdb.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.commons.exception.CoveredCheckedException;
import tech.intellispaces.commons.function.ThrowableFunction;

/**
 * Transaction functions.
 */
public class TransactionFunctions {

  private static final Logger LOG = LoggerFactory.getLogger(TransactionFunctions.class);

  private TransactionFunctions() {}

  public static void transactional(TransactionFactoryHandle transactionFactory, Runnable operation) {
    transactional(transactionFactory,
        data -> {
          operation.run();
          return null;
        },
        null
    );
  }

  public static <R, E extends Throwable> R transactional(
      TransactionFactoryHandle transactionFactory,
      ThrowableFunction<Object[], R, E> operation,
      Object[] data
  ) {
    R result;
    TransactionHandle tx = null;
    try {
      tx = transactionFactory.getTransaction();
      Transactions.setCurrent(tx);
      result = operation.apply(data);
      tx.commit();
    } catch (TransactionException e) {
      LOG.error("Unexpected exception occurred while transaction was executed. Transaction will be rolled back");
      rollback(tx, e);
      throw e;
    } catch (RuntimeException | Error e) {
      if (tx == null) {
        throw TransactionException.withCauseAndMessage(e, "Could not get transaction");
      }
      LOG.error("Runtime exception " + e.getClass().getCanonicalName() + " occurred while transaction was executed. " +
          "Transaction will be rolled back");
      rollback(tx, e);
      throw TransactionException.withCauseAndMessage(e, "Could not execute transaction");
    } catch (Throwable e) {
      if (tx == null) {
        throw TransactionException.withCauseAndMessage(e, "Could not get transaction");
      }
      LOG.info("Checked exception " + e.getClass().getCanonicalName() + " occurred while transaction was executed. " +
          "Transaction will be committed");
      commit(tx, e);
      throw CoveredCheckedException.withCause(e);
    } finally {
      if (tx != null) {
        Transactions.setCurrent(null);
      }
    }
    return result;
  }

  private static void commit(TransactionHandle tx, Throwable reason) {
    try {
      tx.commit();
    } catch (Throwable e) {
      if (reason != null) {
        TransactionException te = TransactionException.withCauseAndMessage(e,
            "Could not commit transaction after exception {}", reason.getClass().getCanonicalName());
        te.addSuppressed(reason);
        throw te;
      }
      throw TransactionException.withCauseAndMessage(e, "Could not commit transaction");
    }
  }

  private static void rollback(TransactionHandle tx, Throwable reason) {
    try {
      tx.rollback();
    } catch (Throwable e) {
      if (reason != null) {
        TransactionException te = TransactionException.withCauseAndMessage(e,
            "Could not roll back transaction after exception {}", reason.getClass().getCanonicalName());
        te.addSuppressed(reason);
        throw te;
      }
      throw TransactionException.withCauseAndMessage(e, "Could not roll back transaction");
    }
  }
}
