package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.AutoGuide;
import intellispaces.jaquarius.annotation.Inject;
import intellispaces.jaquarius.annotation.Mapper;
import intellispaces.jaquarius.annotation.Mover;
import intellispaces.jaquarius.annotation.ObjectHandle;
import intellispaces.ixora.rdb.exception.RdbException;
import intellispaces.ixora.data.association.Map;
import intellispaces.ixora.data.cursor.Cursor;
import intellispaces.ixora.data.collection.List;

@ObjectHandle(TransactionDomain.class)
abstract class TransactionHandle implements MovableTransaction {
  private final MovableConnection connection;

  @Inject
  @AutoGuide
  abstract ParameterizedQueryToBlindQueryGuide parameterizedQueryToBlindQueryGuide();

  TransactionHandle(MovableConnection connection) {
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
  public MovableTransaction commit() {
    connection.commit();
    connection.close();
    return this;
  }

  @Mover
  @Override
  public MovableTransaction rollback() {
    connection.rollback();
    connection.close();
    return this;
  }

  @Mover
  @Override
  public MovableTransaction modify(String query) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public MovableResultSet query(String query) {
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
    MovableResultSet rs = connection.createStatement().executeQuery(query);
    return fetchData(dataType, rs);
  }

  @Mapper
  @Override
  public <D> D fetchData(Class<D> dataType, String query, Map<String, Object> params) {
    BlindQueryAndParameterNames blindQueryAndParamNames = (
      parameterizedQueryToBlindQueryGuide().parameterizedQueryToBlindQuery(query)
    );
    MovablePreparedStatement ps = connection.createPreparedStatement(blindQueryAndParamNames.blindQuery());
    setParamValues(ps, blindQueryAndParamNames.parameterNames(), params);
    MovableResultSet rs = ps.executeQuery();
    return fetchData(dataType, rs);
  }

  private <D> D fetchData(Class<D> dataType, MovableResultSet rs) {
    if (!rs.next()) {
      throw RdbException.withMessage("No data found");
    }
    D data = rs.dataValue(dataType);
    if (rs.next()) {
      throw RdbException.withMessage("More than one data was found");
    }
    return data;
  }

  private void setParamValues(MovablePreparedStatement ps, List<String> paramNames, Map<String, Object> params) {
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
