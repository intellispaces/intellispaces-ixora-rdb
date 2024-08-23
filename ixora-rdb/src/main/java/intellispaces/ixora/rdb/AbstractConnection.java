package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.Connection;
import intellispaces.ixora.rdb.MovableConnection;
import intellispaces.ixora.rdb.Statement;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Mover;
import intellispaces.core.exception.TraverseException;

import java.sql.SQLException;

@MovableObjectHandle("BasicConnection")
public abstract class AbstractConnection implements MovableConnection {
  private final java.sql.Connection connection;

  public AbstractConnection(java.sql.Connection connection) {
    this.connection = connection;
  }

  @Mover
  @Override
  public Statement createStatement() {
    try {
      return new BasicStatement(connection.createStatement());
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not create statement");
    }
  }

  @Mover
  @Override
  public Connection close() {

    return null;
  }
}
