package tech.mindstructs.rdb;

import intellispaces.ixora.mindstructs.rdb.MovableStatementHandle;
import intellispaces.ixora.mindstructs.rdb.ResultSetHandle;
import tech.intellispaces.framework.core.annotation.Mapper;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.exception.TraverseException;

import java.sql.SQLException;
import java.sql.Statement;

@ObjectHandle("BasicStatement")
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
      System.out.println(rs);
      return null;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not execute query");
    }
  }
}
