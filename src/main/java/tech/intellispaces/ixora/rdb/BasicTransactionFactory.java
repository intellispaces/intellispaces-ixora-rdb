package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.MoverWithBacklash;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.ixora.rdb.exception.TransactionException;

@ObjectHandle
public abstract class BasicTransactionFactory implements TransactionFactoryMovableHandle {
  private final DataSourceMovableHandle dataSource;

  public BasicTransactionFactory(DataSourceMovableHandle dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  @Mapper
  public DataSourceHandle dataSource() {
    return dataSource;
  }

  @Override
  @MoverWithBacklash
  public TransactionMovableHandle getTransaction() throws TransactionException {
    ConnectionMovableHandle connection = (ConnectionMovableHandle) dataSource.getConnection();
    return new BasicTransactionHandleImpl(connection);
  }
}
