package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;

import java.sql.SQLException;
import java.sql.Statement;

@ObjectHandle(StatementDomain.class)
abstract class StatementHandle implements MovableStatement {
  private final Statement statement;

  StatementHandle(Statement statement) {
    this.statement = statement;
  }

  @Mapper
  @Override
  public MovableResultSet executeQuery(String query) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(query);
      return new ResultSetHandleImpl(rs);
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not execute query {0}", query);
    }
  }
}
