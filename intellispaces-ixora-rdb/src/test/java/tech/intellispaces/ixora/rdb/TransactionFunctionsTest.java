package tech.intellispaces.ixora.rdb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionFactoryHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.TransactionHandle;
import tech.intellispaces.jaquarius.system.Modules;

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

  @BeforeEach
  public void init() {
    Modules.load().start();
  }

  @AfterEach
  public void deinit() {
    Modules.unload();
  }

  @Test
  public void testTransactional_whenOk() {
    // Given
    MovableTransactionFactoryHandle transactionFactory = mock(MovableTransactionFactoryHandle.class);
    MovableTransactionHandle tx = mock(MovableTransactionHandle.class);
    when(transactionFactory.getTransaction()).thenReturn(tx);

    List<TransactionHandle> appliedTransactions = new ArrayList<>();

    // When
    TransactionFunctions.transactional(transactionFactory,
        () -> appliedTransactions.add(Transactions.current())
    );

    // Then
    assertThat(appliedTransactions).containsExactly(tx);
    verify(tx).commit();
    verify(tx, never()).rollback();
  }
}
