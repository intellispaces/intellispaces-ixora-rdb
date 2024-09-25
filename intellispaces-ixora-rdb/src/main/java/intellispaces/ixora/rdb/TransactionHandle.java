package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.AutoGuide;
import intellispaces.framework.core.annotation.Inject;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;
import intellispaces.ixora.structures.association.Map;
import intellispaces.ixora.structures.collection.Cursor;
import intellispaces.ixora.structures.collection.List;

@ObjectHandle(value = TransactionDomain.class, name = "TransactionHandleImpl")
public abstract class TransactionHandle implements MovableTransaction {
  private final MovableConnection connection;

  @Inject
  @AutoGuide
  abstract ParameterizedQueryToBlindQueryGuide parameterizedQueryToBlindQueryGuide();

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
    connection.close();
    return this;
  }

  @Mover
  @Override
  public Transaction rollback() {
    connection.rollback();
    connection.close();
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
    BlindQueryAndParameterNames blindQueryAndParamNames = (
      parameterizedQueryToBlindQueryGuide().parameterizedQueryToBlindQuery(query)
    );
    PreparedStatement ps = connection.createPreparedStatement(blindQueryAndParamNames.blindQuery());
    setParamValues(ps, blindQueryAndParamNames.parameterNames(), params);
    ResultSet rs = ps.executeQuery();
    return fetchData(dataType, rs);
  }

  private <D> D fetchData(Class<D> dataType, ResultSet rs) {
    if (!rs.next()) {
      throw RdbException.withMessage("No data found");
    }
    D data = rs.dataValue(dataType);
    if (rs.next()) {
      throw RdbException.withMessage("More than one data was found");
    }
    return data;
  }

  private void setParamValues(PreparedStatement ps, List<String> paramNames, Map<String, Object> params) {
    int index = 1;
    for (Object paramName : paramNames.nativeList()) {
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
}
