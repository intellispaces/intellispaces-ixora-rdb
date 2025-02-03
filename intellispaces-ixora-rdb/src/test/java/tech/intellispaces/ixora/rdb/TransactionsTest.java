package tech.intellispaces.ixora.rdb;

import org.junit.jupiter.api.Test;
import tech.intellispaces.jaquarius.exception.TraverseException;

/**
 * Tests for {@link Transactions} class.
 */
public class TransactionsTest {

  @Test
  public void test_whenNoTransaction() {
    assertThrows(TraverseException.class, Transactions::current, "Current transaction is not defined");
  }
}
