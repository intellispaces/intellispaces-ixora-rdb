package intellispaces.ixora.rdb;

import intellispaces.jaquarius.exception.TraverseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Transactions} class.
 */
public class TransactionsTest {

  @Test
  public void test_whenNoTransaction() {
    assertThrows(TraverseException.class, Transactions::current, "Current transaction is not defined");
  }
}
