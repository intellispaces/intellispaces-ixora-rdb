package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;
import intellispaces.ixora.structures.association.Map;
import intellispaces.ixora.structures.collection.Cursor;

import java.util.ArrayList;
import java.util.List;

@ObjectHandle(value = TransactionDomain.class, name = "TransactionHandleImpl")
public abstract class TransactionHandle implements MovableTransaction {
  private final MovableConnection connection;

  public TransactionHandle(MovableConnection connection) {
    this.connection = connection;
    connection.disableAutoCommit();
  }

  @Mapper
  @Override
  public Connection connection() {
    return connection;
  }

  @Mover
  @Override
  public Transaction commit() {
    connection.commit();
    return this;
  }

  @Mover
  @Override
  public Transaction rollback() {
    connection.rollback();
    return this;
  }

  @Mover
  @Override
  public Transaction modify(String query) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public ResultSet query(String query) {
    return connection.createStatement().executeQuery(query);
  }

  @Mapper
  @Override
  public <D> Cursor<D> queryData(Class<D> dataType, String query) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public <D> D fetchData(Class<D> dataType, String query) {
    ResultSet rs = connection.createStatement().executeQuery(query);
    return fetchData(dataType, rs);
  }

  @Mapper
  @Override
  public <D> D fetchData(Class<D> dataType, String query, Map<String, Object> params) {
    PreparedQuery preparedQuery = prepareQuery(query, params);
    PreparedStatement ps = connection.createPreparedStatement(preparedQuery.query());
    setParamValues(ps, preparedQuery.values());
    ResultSet rs = ps.executeQuery();
    return fetchData(dataType, rs);
  }

  private <D> D fetchData(Class<D> dataType, ResultSet rs) {
    if (!rs.next()) {
      throw RdbException.withMessage("No data found");
    }
    D data = rs.value(dataType);
    if (rs.next()) {
      throw RdbException.withMessage("More than one data was found");
    }
    return data;
  }

  private PreparedQuery prepareQuery(String query, Map<String, Object> params) {
    List<Object> values = new ArrayList<>();
    char[] originQuery = query.toCharArray();
    char[] preparedQuery = new char[query.length()];
    int index1 = 0, index2 = 0;
    while (index1 < originQuery.length) {
      char ch = originQuery[index1++];
      if (ch == ':') {
        preparedQuery[index2++] = '?';
        int ind = index1;
        while (index1 < originQuery.length && Character.isLetterOrDigit(originQuery[index1])) {
          index1++;
        }
        String paramName = query.substring(ind, index1);
        if (!params.nativeMap().containsKey(paramName)) {
          throw RdbException.withMessage("Value of parameter {0} is not found", paramName);
        }
        Object value = params.nativeMap().get(paramName);
        values.add(value);
      } else {
        preparedQuery[index2++] = ch;
      }
    }
    return new PreparedQuery(new String(preparedQuery, 0, index2), values);
  }

  private void setParamValues(PreparedStatement ps, List<Object> paramValues) {
    int index = 1;
    for (Object paramValue : paramValues) {
      if (paramValue instanceof Integer) {
        ps.setInt(index++, (int) paramValue);
      } else {
        throw new RuntimeException("Not implemented");
      }
    }
  }

  private final static class PreparedQuery {
    private final String query;
    private final List<Object> values;

    public PreparedQuery(String query, List<Object> values) {
      this.query = query;
      this.values = values;
    }

    public String query() {
      return query;
    }

    public List<Object> values() {
      return values;
    }
  }
}
