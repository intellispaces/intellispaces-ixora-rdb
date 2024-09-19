package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;

import java.sql.SQLException;

@ObjectHandle(value = ConnectionDomain.class, name = "ConnectionHandleImpl")
public abstract class ConnectionHandle implements MovableConnection {
  private final java.sql.Connection connection;

  public ConnectionHandle(java.sql.Connection connection) {
    this.connection = connection;
  }

  @Mover
  @Override
  public Statement createStatement() {
    try {
      return new StatementHandleImpl(connection.createStatement());
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not create statement");
    }
  }

  @Mover
  @Override
  public PreparedStatement createPreparedStatement(String query) {
    try {
      return new PreparedStatementHandleImpl(connection.prepareStatement(query));
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not create statement");
    }
  }

  @Mover
  @Override
  public Connection close() {

    return null;
  }
}
