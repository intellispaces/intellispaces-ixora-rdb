package intellispaces.ixora.rdb;

import intellispaces.framework.core.exception.TraverseException;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Transactions {
  private static final ThreadLocal<Deque<Transaction>> CURRENT_TRANSACTIONS = new ThreadLocal<>();

  public static Transaction current() {
    Transaction tx = null;
    Deque<Transaction> transactions = CURRENT_TRANSACTIONS.get();
    if (transactions != null) {
      tx = transactions.peek();
    }
    if (tx == null) {
      throw TraverseException.withMessage("Current transaction is not defined");
    }
    return tx;
  }

  static void setCurrent(Transaction tx) {
    if (tx != null) {
      Deque<Transaction> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions == null) {
        transactions = new ArrayDeque<>();
        CURRENT_TRANSACTIONS.set(transactions);
      }
      transactions.push(tx);
    } else {
      Deque<Transaction> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions != null) {
        transactions.pop();
      }
    }
  }
}
