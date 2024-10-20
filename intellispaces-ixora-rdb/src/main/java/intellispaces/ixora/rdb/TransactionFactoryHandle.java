package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.MapperOfMoving;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.TransactionException;

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
