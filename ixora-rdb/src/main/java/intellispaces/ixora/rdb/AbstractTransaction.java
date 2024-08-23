package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Mover;
import intellispaces.ixora.structures.collection.Cursor;

@MovableObjectHandle("BasicTransaction")
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
    return null;
  }

  @Mover
  @Override
  public Transaction rollback() {
    return null;
  }

  @Mover
  @Override
  public Transaction update(String sql) {
    return null;
  }

  @Mapper
  @Override
  public ResultSet query(String sql) {
    return connection.createStatement().executeQuery(sql);
  }

  @Mapper
  @Override
  public <T> Cursor<T> queryData(Class<T> type, String sql) {
    return null;
  }

  @Mapper
  @Override
  public <T> T fetchData(Class<T> type, String sql) {
    return null;
  }
}
