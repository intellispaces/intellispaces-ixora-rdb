package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.MapperOfMoving;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.ixora.rdb.DataSourceHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableConnectionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableDataSourceHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionFactoryHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.TransactionFactoryDomain;

@ObjectHandle(TransactionFactoryDomain.class)
abstract class TransactionFactoryHandleOverDataSource implements MovableTransactionFactoryHandle {
  private final MovableDataSourceHandle dataSource;

  TransactionFactoryHandleOverDataSource(MovableDataSourceHandle dataSource) {
    this.dataSource = dataSource;
  }

  @Mapper
  @Override
  public DataSourceHandle dataSource() {
    return dataSource;
  }

  @Override
  @MapperOfMoving
  public MovableTransactionHandle getTransaction() throws TransactionException {
    MovableConnectionHandle connection = dataSource.getConnection();
    return new TransactionHandleOverConnectionWrapper(connection);
  }
}
