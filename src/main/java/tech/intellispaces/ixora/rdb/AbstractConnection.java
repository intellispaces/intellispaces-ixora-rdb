package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.MovableConnectionHandle;
import intellispaces.ixora.rdb.StatementHandle;
import tech.intellispaces.framework.core.annotation.Mover;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.sql.Connection;
import java.sql.SQLException;

@ObjectHandle("BasicConnection")
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
  public MovableConnectionHandle close() {

    return null;
  }
}
