package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Configuration;
import intellispaces.framework.core.annotation.Projection;
import intellispaces.framework.core.annotation.Properties;
import intellispaces.ixora.rdb.guide.IxoraParameterizedQueryToBlindQueryGuide;
import intellispaces.ixora.rdb.guide.IxoraResultSetToDataGuide;

@Configuration(include = {
    IxoraParameterizedQueryToBlindQueryGuide.class,
    IxoraResultSetToDataGuide.class
})
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
    return new TransactionFactoryHandleImpl(dataSource);
  }
}
