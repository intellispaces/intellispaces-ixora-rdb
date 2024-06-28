package tech.intellispaces.ixora.rdb;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link TransactionFunctions} class.
 */
public class TransactionFunctionsTest {

  @Test
  public void testTransactional_whenNoException() {
    // Given
    TransactionFactoryHandle transactionFactory = mock(TransactionFactoryHandle.class);
    TransactionHandle tx = mock(TransactionHandle.class);
    when(transactionFactory.getTransaction()).thenReturn(tx);

    List<TransactionHandle> appliedTransactions = new ArrayList<>();

    // When
    TransactionFunctions.transactional(transactionFactory, appliedTransactions::add);

    // Then
    assertThat(appliedTransactions).containsExactly(tx);
    verify(tx).commit();
    verify(tx, never()).rollback();
  }
}
