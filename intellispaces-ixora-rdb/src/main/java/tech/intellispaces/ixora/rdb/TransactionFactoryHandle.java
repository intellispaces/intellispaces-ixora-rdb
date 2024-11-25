package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.MapperOfMoving;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;

@ObjectHandle(TransactionFactoryDomain.class)
abstract class TransactionFactoryHandle implements MovableTransactionFactory {
  private final MovableDataSource dataSource;

  TransactionFactoryHandle(MovableDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Mapper
  @Override
  public DataSource dataSource() {
    return dataSource;
  }

  @Override
  @MapperOfMoving
  public MovableTransaction getTransaction() throws TransactionException {
    MovableConnection connection = dataSource.getConnection();
    return new TransactionHandleImpl(connection);
  }
}
