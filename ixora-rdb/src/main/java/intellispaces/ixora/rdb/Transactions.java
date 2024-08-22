package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.TransactionHandle;
import intellispaces.core.exception.TraverseException;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Transactions {
  private static final ThreadLocal<Deque<TransactionHandle>> CURRENT_TRANSACTIONS = new ThreadLocal<>();

  public static TransactionHandle current() {
    TransactionHandle tx = null;
    Deque<TransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
    if (transactions != null) {
      tx = transactions.peek();
    }
    if (tx == null) {
      throw TraverseException.withMessage("Current transaction is not defined");
    }
    return tx;
  }

  static void setCurrent(TransactionHandle tx) {
    if (tx != null) {
      Deque<TransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions == null) {
        transactions = new ArrayDeque<>();
        CURRENT_TRANSACTIONS.set(transactions);
      }
      transactions.push(tx);
    } else {
      Deque<TransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions != null) {
        transactions.pop();
      }
    }
  }
}
