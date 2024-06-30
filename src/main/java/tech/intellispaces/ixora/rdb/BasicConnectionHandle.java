package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.sql.Connection;
import java.sql.SQLException;

@ObjectHandle
public abstract class BasicConnectionHandle implements ConnectionMovableHandle {
  private final Connection connection;

  public BasicConnectionHandle(Connection connection) {
    this.connection = connection;
  }

  @Mover
  @Override
  public StatementHandle createStatement() {
    try {
      return new BasicStatementHandleImpl(connection.createStatement());
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not create statement");
    }
  }

  @Mover
  @Override
  public ConnectionMovableHandle close() {

    return null;
  }
}
