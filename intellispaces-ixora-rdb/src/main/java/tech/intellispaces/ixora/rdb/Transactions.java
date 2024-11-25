package tech.intellispaces.ixora.rdb;

import tech.intellispaces.jaquarius.exception.TraverseExceptions;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Transactions {
  private static final ThreadLocal<Deque<MovableTransaction>> CURRENT_TRANSACTIONS = new ThreadLocal<>();

  public static MovableTransaction current() {
    MovableTransaction tx = null;
    Deque<MovableTransaction> transactions = CURRENT_TRANSACTIONS.get();
    if (transactions != null) {
      tx = transactions.peek();
    }
    if (tx == null) {
      throw TraverseExceptions.withMessage("Current transaction is not defined");
    }
    return tx;
  }

  static void setCurrent(MovableTransaction tx) {
    if (tx != null) {
      Deque<MovableTransaction> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions == null) {
        transactions = new ArrayDeque<>();
        CURRENT_TRANSACTIONS.set(transactions);
      }
      transactions.push(tx);
    } else {
      Deque<MovableTransaction> transactions = CURRENT_TRANSACTIONS.get();
      if (transactions != null) {
        transactions.pop();
      }
    }
  }
}
