package tech.intellispaces.ixora.rdb.configuration;

import tech.intellispaces.framework.core.annotation.Configuration;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Properties;
import tech.intellispaces.ixora.rdb.BasicTransactionFactory;
import tech.intellispaces.ixora.rdb.DataSourceMovableHandle;
import tech.intellispaces.ixora.rdb.DataSourcePropertiesHandle;
import tech.intellispaces.ixora.rdb.TransactionFactoryMovableHandle;

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
  public TransactionFactoryMovableHandle transactionFactory(DataSourceMovableHandle dataSource) {
    return new BasicTransactionFactory(dataSource);
  }
}
