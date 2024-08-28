package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.Mover;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.ixora.structures.collection.Cursor;

@ObjectHandle(value = TransactionDomain.class, name = "BasicTransaction")
public abstract class AbstractTransaction implements MovableTransaction {
  private final MovableConnection connection;

  public AbstractTransaction(MovableConnection connection) {
    this.connection = connection;
  }

  @Mapper
  @Override
  public Connection connection() {
    return connection;
  }

  @Mover
  @Override
  public Transaction commit() {
//    throw new RuntimeException("Not implemented");
    return this;
  }

  @Mover
  @Override
  public Transaction rollback() {
    throw new RuntimeException("Not implemented");
  }

  @Mover
  @Override
  public Transaction modify(String sql) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public ResultSet query(String sql) {
    return connection.createStatement().executeQuery(sql);
  }

  @Mapper
  @Override
  public <D> Cursor<D> queryData(Class<D> dataType, String sql) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public <D> D fetchData(Class<D> dataType, String sql) {
    throw new RuntimeException("Not implemented");
  }
}
