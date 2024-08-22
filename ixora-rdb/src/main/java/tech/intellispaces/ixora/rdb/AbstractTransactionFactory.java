package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.DataSourceHandle;
import intellispaces.ixora.rdb.MovableConnectionHandle;
import intellispaces.ixora.rdb.MovableDataSourceHandle;
import intellispaces.ixora.rdb.MovableTransactionFactoryHandle;
import intellispaces.ixora.rdb.TransactionHandle;
import intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.core.annotation.Mapper;
import tech.intellispaces.core.annotation.MovableObjectHandle;
import tech.intellispaces.core.annotation.Mover;

@MovableObjectHandle("BasicTransactionFactory")
public abstract class AbstractTransactionFactory implements MovableTransactionFactoryHandle {
  private final MovableDataSourceHandle dataSource;

  public AbstractTransactionFactory(MovableDataSourceHandle dataSource) {
    this.dataSource = dataSource;
  }

  @Mapper
  @Override
  public DataSourceHandle dataSource() {
    return dataSource;
  }

  @Mover
  @Override
  public TransactionHandle getTransaction() throws TransactionException {
    MovableConnectionHandle connection = (MovableConnectionHandle) dataSource.getConnection();
    //connection.disableAutoCommit();
    //connection.beginTransaction();
    return new BasicTransaction(connection);
  }
}
