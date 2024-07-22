package tech.mindstructs.rdb;

import intellispaces.ixora.mindstructs.rdb.DataSourceHandle;
import intellispaces.ixora.mindstructs.rdb.MovableConnectionHandle;
import intellispaces.ixora.mindstructs.rdb.MovableDataSourceHandle;
import intellispaces.ixora.mindstructs.rdb.MovableTransactionFactoryHandle;
import intellispaces.ixora.mindstructs.rdb.TransactionHandle;
import intellispaces.ixora.mindstructs.rdb.exception.TransactionException;
import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;

@ObjectHandle("BasicTransactionFactory")
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
