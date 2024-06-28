package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;

import java.sql.Connection;

@ObjectHandle
public abstract class BasicConnectionHandle implements ConnectionMovableHandle {
  private final Connection connection;

  public BasicConnectionHandle(Connection connection) {
    this.connection = connection;
  }

  @Mover
  @Override
  public ConnectionMovableHandle close() {

    return null;
  }
}
