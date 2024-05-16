package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.TransactionHandle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link TransactionFunctions} class.
 */
public class TransactionFunctionsTest {

  @Test
  public void testTransactional_whenNoException() {
    // Given
    TransactionHandle tx = mock(TransactionHandle.class);

    List<TransactionHandle> aaa = new ArrayList<>();
    Consumer<TransactionHandle> operation = aaa::add;

    // When
    TransactionFunctions.transactional(tx, operation);

    // Then
    assertThat(aaa).containsExactly(tx);
    verify(tx).commit();
    verify(tx, never()).rollback();
  }
}
