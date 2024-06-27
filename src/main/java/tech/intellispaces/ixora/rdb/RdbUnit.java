package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.Properties;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Unit;

@Unit
public abstract class RdbUnit {

  @Projection
  @Properties("datasource.jdbc")
  public abstract DataSourcePropertiesHandle dataSourceProperties();

  @Projection
  public DataSourceMovableHandle dataSource(DataSourcePropertiesHandle dataSourceProperties) {
    return new JdbcDataSourceImpl(dataSourceProperties);
  }

  @Projection
  public ConnectionPoolMovableHandle connectionPool(DataSourceMovableHandle dataSource) {
    return null;
  }

  @Projection
  public TransactionFactoryHandle transactionFactory(ConnectionPoolMovableHandle connectionPool) {
    return null;
  }
}
