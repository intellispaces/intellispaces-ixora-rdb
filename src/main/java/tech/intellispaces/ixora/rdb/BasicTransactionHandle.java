package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.ixora.structures.collection.CursorHandle;

@ObjectHandle
public abstract class BasicTransactionHandle implements TransactionMovableHandle {
  private final ConnectionMovableHandle connection;

  public BasicTransactionHandle(ConnectionMovableHandle connection) {
    this.connection = connection;
  }

  @Override
  public ConnectionHandle connection() {
    return connection;
  }

  @Override
  public TransactionMovableHandle commit() {
    return null;
  }

  @Override
  public TransactionMovableHandle rollback() {
    return null;
  }

  @Override
  public TransactionMovableHandle update(String sql) {
    return null;
  }

  @Override
  public ResultSetHandle query(String sql) {
    return null;
  }

  @Override
  public <T> CursorHandle<T> query(Class<T> type, String sql) {
    return null;
  }

  @Override
  public <T> T fetch(Class<T> type, String sql) {
    return null;
  }
}
