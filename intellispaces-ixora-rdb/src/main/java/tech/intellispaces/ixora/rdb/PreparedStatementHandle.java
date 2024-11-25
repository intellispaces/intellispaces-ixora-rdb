package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.exception.RdbExceptions;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.Mover;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;

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
