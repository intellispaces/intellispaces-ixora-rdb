package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.sql.SQLException;
import java.sql.Statement;

@ObjectHandle
public abstract class BasicStatementHandle implements StatementMovableHandle {
  private final Statement statement;

  public BasicStatementHandle(Statement statement) {
    this.statement = statement;
  }

  @Mapper
  @Override
  public ResultSetHandle executeQuery(String sql) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(sql);
      System.out.println(rs);
      return null;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not execute query");
    }
  }
}
