package tech.intellispaces.ixora.rdb;

import tech.intellispaces.commons.base.type.Type;
import tech.intellispaces.ixora.rdb.exception.RdbExceptions;
import tech.intellispaces.jaquarius.annotation.AutoGuide;
import tech.intellispaces.jaquarius.annotation.Inject;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.Mover;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.ixora.data.association.MapHandle;
import tech.intellispaces.jaquarius.ixora.data.collection.ListHandle;
import tech.intellispaces.jaquarius.ixora.data.cursor.MovableCursorHandle;
import tech.intellispaces.jaquarius.ixora.rdb.ConnectionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableConnectionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovablePreparedStatementHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableResultSetHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionHandle;
import tech.intellispaces.jaquarius.ixora.rdb.StringToParameterizedNamedQueryGuide;
import tech.intellispaces.jaquarius.ixora.rdb.TransactionDomain;
import tech.intellispaces.jaquarius.ixora.rdb.sql.ParameterizedNamedQueryHandle;

@ObjectHandle(TransactionDomain.class)
abstract class TransactionHandleOverConnection implements MovableTransactionHandle {
  private final MovableConnectionHandle connection;

  @Inject
  @AutoGuide
  abstract StringToParameterizedNamedQueryGuide stringToParameterizedNamedQueryGuide();

  TransactionHandleOverConnection(MovableConnectionHandle connection) {
    this.connection = connection;
    connection.disableAutoCommit();
  }

  @Mapper
  @Override
  public ConnectionHandle connection() {
    return connection;
  }

  @Mover
  @Override
  public MovableTransactionHandle commit() {
    connection.commit();
    connection.close();
    return this;
  }

  @Mover
  @Override
  public MovableTransactionHandle rollback() {
    connection.rollback();
    connection.close();
    return this;
  }

  @Mover
  @Override
  public MovableTransactionHandle modify(String query) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public MovableResultSetHandle query(String query) {
    return connection.createStatement().executeQuery(query);
  }

  @Mapper
  @Override
  public <D> MovableCursorHandle<D> queryData(Type<D> dataType, String query) {
    throw new RuntimeException("Not implemented");
  }

  @Mapper
  @Override
  public <D> D fetchData(Type<D> dataType, String query) {
    MovableResultSetHandle rs = connection.createStatement().executeQuery(query);
    return fetchData(dataType, rs);
  }

  @Mapper
  @Override
  public <D> D fetchData(Type<D> dataType, String query, MapHandle<String, Object> params) {
    ParameterizedNamedQueryHandle parameterizedQuery = (
        stringToParameterizedNamedQueryGuide().stringToParameterizedNamedQuery(query)
    );
    MovablePreparedStatementHandle ps = connection.createPreparedStatement(parameterizedQuery.query());
    setParamValues(ps, parameterizedQuery.paramNames(), params);
    MovableResultSetHandle rs = ps.executeQuery();
    return fetchData(dataType, rs);
  }

   private <D> D fetchData(Type<D> dataType, MovableResultSetHandle rs) {
    if (!rs.next()) {
      throw RdbExceptions.withMessage("No data found");
    }
    D data = rs.dataValue(dataType);
    if (rs.next()) {
      throw RdbExceptions.withMessage("More than one data was found");
    }
    return data;
  }

  private void setParamValues(
      MovablePreparedStatementHandle ps, ListHandle<String> paramNames, MapHandle<String, Object> params
  ) {
    int index = 1;
    for (Object paramName : paramNames.nativeList()) {
      if (!params.nativeMap().containsKey(paramName)) {
        throw RdbExceptions.withMessage("Value of parameter {0} is not found", paramName);
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
