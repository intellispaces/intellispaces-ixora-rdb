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
    BlindQueryAndParameterNames blindQueryAndParamNames = prepareQuery(query, params);
    PreparedStatement ps = connection.createPreparedStatement(blindQueryAndParamNames.query());
    setParamValues(ps, blindQueryAndParamNames.paramNames(), params);
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

  private BlindQueryAndParameterNames prepareQuery(String query, Map<String, Object> params) {
    char[] originQuery = query.toCharArray();
    char[] blindQuery = new char[query.length()];
    List<String> paramNames = new ArrayList<>();
    int index1 = 0, index2 = 0;
    while (index1 < originQuery.length) {
      char ch = originQuery[index1++];
      if (ch == ':') {
        blindQuery[index2++] = '?';
        int ind = index1;
        while (index1 < originQuery.length && Character.isLetterOrDigit(originQuery[index1])) {
          index1++;
        }
        String paramName = query.substring(ind, index1);
        paramNames.add(paramName);
      } else {
        blindQuery[index2++] = ch;
      }
    }
    return new BlindQueryAndParameterNames(new String(blindQuery, 0, index2), paramNames);
  }

  private void setParamValues(PreparedStatement ps, List<String> paramNames, Map<String, Object> params) {
    int index = 1;
    for (Object paramName : paramNames) {
      if (!params.nativeMap().containsKey(paramName)) {
        throw RdbException.withMessage("Value of parameter {0} is not found", paramName);
      }
      Object paramValue = params.nativeMap().get(paramName);
      if (paramValue instanceof Integer) {
        ps.setInt(index++, (int) paramValue);
      } else {
        throw new RuntimeException("Not implemented");
      }
    }
  }

  private final static class BlindQueryAndParameterNames {
    private final String query;
    private final List<String> paramNames;

    public BlindQueryAndParameterNames(String query, List<String> paramNames) {
      this.query = query;
      this.paramNames = paramNames;
    }

    public String query() {
      return query;
    }

    public List<String> paramNames() {
      return paramNames;
    }
  }
}
