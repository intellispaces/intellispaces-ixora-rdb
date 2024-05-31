package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Properties;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public abstract class DefaultJdbcUnit {

  @Projection
  @Properties("datasource.jdbc")
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
