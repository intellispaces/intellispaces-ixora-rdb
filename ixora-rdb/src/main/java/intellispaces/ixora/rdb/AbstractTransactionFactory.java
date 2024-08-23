package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Mover;
import intellispaces.ixora.rdb.exception.TransactionException;

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
