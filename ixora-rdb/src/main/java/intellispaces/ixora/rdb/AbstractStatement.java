package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.MovableStatementHandle;
import intellispaces.ixora.rdb.ResultSetHandle;
import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.exception.TraverseException;

import java.sql.SQLException;
import java.sql.Statement;

@MovableObjectHandle("BasicStatement")
public abstract class AbstractStatement implements MovableStatementHandle {
  private final Statement statement;

  public AbstractStatement(Statement statement) {
    this.statement = statement;
  }

  @Mapper
  @Override
  public ResultSetHandle executeQuery(String sql) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(sql);
      return new BasicResultSet(rs);
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not execute query {}", sql);
    }
  }
}
