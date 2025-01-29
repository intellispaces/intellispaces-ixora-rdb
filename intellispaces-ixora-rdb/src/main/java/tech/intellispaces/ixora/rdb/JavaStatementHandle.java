package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.exception.RdbExceptions;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;

import java.sql.SQLException;
import java.sql.Statement;

@ObjectHandle(StatementDomain.class)
abstract class JavaStatementHandle implements MovableStatementHandle {
  private final Statement statement;

  JavaStatementHandle(Statement statement) {
    this.statement = statement;
  }

  @Mapper
  @Override
  public MovableResultSetHandle executeQuery(String query) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(query);
      return new JavaResultSetHandleImpl(rs);
    } catch (SQLException e) {
      throw RdbExceptions.withCauseAndMessage(e, "Could not execute query {0}", query);
    }
  }
}
