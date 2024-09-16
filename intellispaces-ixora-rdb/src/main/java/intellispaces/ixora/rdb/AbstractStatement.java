package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.exception.TraverseException;

import java.sql.SQLException;
import java.sql.Statement;

@ObjectHandle(value = StatementDomain.class, name = "BasicStatement")
public abstract class AbstractStatement implements MovableStatement {
  private final Statement statement;

  public AbstractStatement(Statement statement) {
    this.statement = statement;
  }

  @Mapper
  @Override
  public ResultSet executeQuery(String sql) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(sql);
      return new BasicResultSet(rs);
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not execute query {}", sql);
    }
  }
}
