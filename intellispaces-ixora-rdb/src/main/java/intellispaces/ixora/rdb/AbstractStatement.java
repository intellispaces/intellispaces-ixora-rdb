package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;

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
  public ResultSet executeQuery(String query) {
    try {
      java.sql.ResultSet rs = statement.executeQuery(query);
      return new BasicResultSet(rs);
    } catch (SQLException e) {
      throw RdbException.withCauseAndMessage(e, "Could not execute query {0}", query);
    }
  }
}
