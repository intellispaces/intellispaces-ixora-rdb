package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.ixora.rdb.exception.TransactionException;

@ObjectHandle
public abstract class BasicTransactionFactory implements TransactionFactoryMovableHandle {
  private final DataSourceMovableHandle dataSource;

  public BasicTransactionFactory(DataSourceMovableHandle dataSource) {
    this.dataSource = dataSource;
  }

  @Mapper
  @Override
  public DataSourceHandle dataSource() {
    return dataSource;
  }

  @Mover
  @Override
  public TransactionMovableHandle getTransaction() throws TransactionException {
    ConnectionMovableHandle connection = (ConnectionMovableHandle) dataSource.getConnection();
    //connection.disableAutoCommit();
    //connection.beginTransaction();
    return new BasicTransactionHandleImpl(connection);
  }
}
