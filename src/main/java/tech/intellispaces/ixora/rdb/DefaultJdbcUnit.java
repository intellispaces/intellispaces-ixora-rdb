package tech.intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.ConnectionPoolHandle;
import intellispaces.ixora.rdb.JdbcDataSourceHandle;
import intellispaces.ixora.rdb.JdbcDataSourceProperties;
import intellispaces.ixora.rdb.TransactionFactoryHandle;
import tech.intellispacesframework.core.annotation.ModuleProperties;
import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Unit;

@Unit
public abstract class DefaultJdbcUnit {

  @Projection
  @ModuleProperties("datasource.jdbc")
  public abstract JdbcDataSourceProperties jdbcDataSourceProperties();

  @Projection
  public JdbcDataSourceHandle jdbcDataSource(JdbcDataSourceProperties jdbcDataSourceProperties) {
    return null;
  }

  @Projection
  public ConnectionPoolHandle connectionPool(JdbcDataSourceHandle jdbcDataSource) {
    return null;
  }

  @Projection
  public TransactionFactoryHandle transactionFactory(ConnectionPoolHandle connectionPool) {
    return null;
  }
}
