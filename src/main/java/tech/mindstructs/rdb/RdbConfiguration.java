package tech.mindstructs.rdb;

import intellispaces.ixora.mindstructs.rdb.DataSourcePropertiesHandle;
import intellispaces.ixora.mindstructs.rdb.MovableDataSourceHandle;
import intellispaces.ixora.mindstructs.rdb.MovableTransactionFactoryHandle;
import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Properties;

@Configuration
public abstract class RdbConfiguration {

  /**
   * Data source properties.
   */
  @Projection
  @Properties("datasource")
  public abstract DataSourcePropertiesHandle dataSourceProperties();

  /**
   * Transaction factory.
   */
  @Projection
  public MovableTransactionFactoryHandle transactionFactory(MovableDataSourceHandle dataSource) {
    return new BasicTransactionFactory(dataSource);
  }
}
