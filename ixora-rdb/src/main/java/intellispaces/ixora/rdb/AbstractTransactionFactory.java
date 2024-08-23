package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.DataSource;
import intellispaces.ixora.rdb.MovableConnection;
import intellispaces.ixora.rdb.MovableDataSource;
import intellispaces.ixora.rdb.MovableTransactionFactory;
import intellispaces.ixora.rdb.Transaction;
import intellispaces.ixora.rdb.exception.TransactionException;
import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Mover;

@MovableObjectHandle("BasicTransactionFactory")
public abstract class AbstractTransactionFactory implements MovableTransactionFactory {
  private final MovableDataSource dataSource;

  public AbstractTransactionFactory(MovableDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Mapper
  @Override
  public DataSource dataSource() {
    return dataSource;
  }

  @Mover
  @Override
  public Transaction getTransaction() throws TransactionException {
    MovableConnection connection = (MovableConnection) dataSource.getConnection();
    //connection.disableAutoCommit();
    //connection.beginTransaction();
    return new BasicTransaction(connection);
  }
}
