package intellispaces.ixora.rdb;

import intellispaces.ixora.rdb.DataSourceProperties;
import intellispaces.ixora.rdb.MovableDataSource;
import intellispaces.ixora.rdb.MovableTransactionFactory;
import intellispaces.core.annotation.Configuration;
import intellispaces.core.annotation.Projection;
import intellispaces.core.annotation.Properties;

@Configuration
public abstract class RdbConfiguration {

  /**
   * Data source properties.
   */
  @Projection
  @Properties("datasource")
  public abstract DataSourceProperties dataSourceProperties();

  /**
   * Transaction factory.
   */
  @Projection
  public MovableTransactionFactory transactionFactory(MovableDataSource dataSource) {
    return new BasicTransactionFactory(dataSource);
  }
}
