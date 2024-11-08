package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.exception.RdbExceptions;
import intellispaces.jaquarius.annotation.Mapper;
import intellispaces.jaquarius.annotation.Mover;
import intellispaces.jaquarius.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@ObjectHandle(PreparedStatementDomain.class)
abstract class PreparedStatementHandle implements MovablePreparedStatement {
  private final PreparedStatement preparedStatement;

  PreparedStatementHandle(PreparedStatement preparedStatement) {
    this.preparedStatement = preparedStatement;
  }

  @Mapper
  @Override
  public MovableResultSet executeQuery() {
    try {
      java.sql.ResultSet rs = preparedStatement.executeQuery();
      return new ResultSetHandleImpl(rs);
    } catch (SQLException e) {
      throw RdbExceptions.withCauseAndMessage(e, "Could not execute prepared statement");
    }
  }

  @Mover
  @Override
  public MovablePreparedStatement setInt(int parameterIndex, int value) {
    try {
      preparedStatement.setInt(parameterIndex, value);
      return this;
    } catch (SQLException e) {
      throw RdbExceptions.withCauseAndMessage(e, "Could not set parameter of the prepared statement");
    }
  }
}
