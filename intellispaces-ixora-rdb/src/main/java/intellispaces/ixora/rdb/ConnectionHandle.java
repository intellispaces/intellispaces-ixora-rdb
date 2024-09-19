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
  public Connection disableAutoCommit() {
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not disable SQL connection auto commit");
    }
    return this;
  }

  @Mover
  @Override
  public Connection commit() {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not commit SQL connection");
    }
    return this;
  }

  @Mover
  @Override
  public Connection rollback() {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not roll back SQL connection");
    }
    return this;
  }

  @Mover
  @Override
  public Connection close() {
    try {
      connection.close();
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not close SQL connection");
    }
    return this;
  }
}
