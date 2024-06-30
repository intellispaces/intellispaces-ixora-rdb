package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.ixora.structures.collection.CursorHandle;

@ObjectHandle
public abstract class BasicTransactionHandle implements TransactionMovableHandle {
  private final ConnectionMovableHandle connection;

  public BasicTransactionHandle(ConnectionMovableHandle connection) {
    this.connection = connection;
  }

  @Mapper
  @Override
  public ConnectionHandle connection() {
    return connection;
  }

  @Mover
  @Override
  public TransactionMovableHandle commit() {
    return null;
  }

  @Mover
  @Override
  public TransactionMovableHandle rollback() {
    return null;
  }

  @Mover
  @Override
  public TransactionMovableHandle update(String sql) {
    return null;
  }

  @Mapper
  @Override
  public ResultSetHandle query(String sql) {
    return connection.createStatement().executeQuery(sql);
  }

  @Mapper
  @Override
  public <T> CursorHandle<T> query(Class<T> type, String sql) {
    return null;
  }

  @Mapper
  @Override
  public <T> T fetch(Class<T> type, String sql) {
    return null;
  }
}
