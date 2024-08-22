package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.MovableResultSetHandle;
import tech.intellispaces.core.annotation.Mapper;
import tech.intellispaces.core.annotation.MovableObjectHandle;
import tech.intellispaces.core.annotation.Mover;
import tech.intellispaces.core.exception.TraverseException;

import java.sql.ResultSet;
import java.sql.SQLException;

@MovableObjectHandle("BasicResultSet")
public abstract class AbstractResultSet implements MovableResultSetHandle {
  private final java.sql.ResultSet rs;

  public AbstractResultSet(ResultSet rs) {
    this.rs = rs;
  }

  @Mover
  @Override
  public boolean next() {
    try {
      return rs.next();
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not move cursor");
    }
  }

  @Mapper
  @Override
  public Integer integerValue(String name) {
    try {
      int value = rs.getInt(name);
      if (rs.wasNull()) {
        return null;
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {}", name);
    }
  }

  @Mapper
  @Override
  public String stringValue(String name) {
    try {
      String value = rs.getString(name);
      if (rs.wasNull()) {
        return null;
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read string value by name {}", name);
    }
  }

  @Mapper
  @Override
  public <T> T rowValue(Class<T> valueDomain) {
    throw new RuntimeException("Not implemented");
  }
}
