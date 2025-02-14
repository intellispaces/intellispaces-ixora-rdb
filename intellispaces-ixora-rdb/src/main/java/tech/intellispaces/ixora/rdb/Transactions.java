package tech.intellispaces.ixora.rdb;

import tech.intellispaces.jaquarius.exception.TraverseExceptions;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionHandle;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Transactions {
  private static final ThreadLocal<Deque<MovableTransactionHandle>> CURRENT_TRANSACTIONS = new ThreadLocal<>();

  public static MovableTransactionHandle current() {
    MovableTransactionHandle tx = null;
    Deque<MovableTransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
    if (transactions != null) {
      tx = transactions.peek();
    }
    if (tx == null) {
      throw TraverseExceptions.withMessage("Current transaction is not defined");
    }
    return tx;
  }

  static void setCurrent(MovableTransactionHandle tx) {
    if (tx != null) {
      Deque<MovableTransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions == null) {
        transactions = new ArrayDeque<>();
        CURRENT_TRANSACTIONS.set(transactions);
      }
      transactions.push(tx);
    } else {
      Deque<MovableTransactionHandle> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions != null) {
        transactions.pop();
      }
    }
  }
}
