package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.ConnectionHandle;
import intellispaces.ixora.rdb.MovableConnectionHandle;
import intellispaces.ixora.rdb.MovableTransactionHandle;
import intellispaces.ixora.rdb.ResultSetHandle;
import intellispaces.ixora.rdb.TransactionHandle;
import intellispaces.ixora.structures.collection.CursorHandle;
import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;

@ObjectHandle("BasicTransaction")
public abstract class AbstractTransaction implements MovableTransactionHandle {
  private final MovableConnectionHandle connection;

  public AbstractTransaction(MovableConnectionHandle connection) {
    this.connection = connection;
  }

  @Mapper
  @Override
  public ConnectionHandle connection() {
    return connection;
  }

  @Mover
  @Override
  public TransactionHandle commit() {
    return null;
  }

  @Mover
  @Override
  public TransactionHandle rollback() {
    return null;
  }

  @Mover
  @Override
  public TransactionHandle update(String sql) {
    return null;
  }

  @Mapper
  @Override
  public ResultSetHandle query(String sql) {
    return connection.createStatement().executeQuery(sql);
  }

  @Mapper
  @Override
  public <T> CursorHandle<T> queryData(Class<T> type, String sql) {
    return null;
  }

  @Mapper
  @Override
  public <T> T fetchData(Class<T> type, String sql) {
    return null;
  }
}
