package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.ConnectionHandle;
import intellispaces.ixora.rdb.MovableConnectionHandle;
import intellispaces.ixora.rdb.StatementHandle;
import tech.intellispaces.core.annotation.MovableObjectHandle;
import tech.intellispaces.core.annotation.Mover;
import tech.intellispaces.core.exception.TraverseException;

import java.sql.Connection;
import java.sql.SQLException;

@MovableObjectHandle("BasicConnection")
public abstract class AbstractConnection implements MovableConnectionHandle {
  private final Connection connection;

  public AbstractConnection(Connection connection) {
    this.connection = connection;
  }

  @Mover
  @Override
  public StatementHandle createStatement() {
    try {
      return new BasicStatement(connection.createStatement());
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not create statement");
    }
  }

  @Mover
  @Override
  public ConnectionHandle close() {

    return null;
  }
}
