package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.MapperOfMoving;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.exception.TraverseException;

import java.sql.ResultSet;
import java.sql.SQLException;

@ObjectHandle(value = ResultSetDomain.class, name = "ResultSetHandleImpl")
abstract class ResultSetHandle implements MovableResultSet {
  private final java.sql.ResultSet rs;

  ResultSetHandle(ResultSet rs) {
    this.rs = rs;
  }

  @Override
  @MapperOfMoving
  public Boolean next() {
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
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {0}", name);
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
      throw TraverseException.withCauseAndMessage(e, "Could not read string value by name {0}", name);
    }
  }
}
