package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.Configuration;
import intellispaces.jaquarius.annotation.Projection;
import intellispaces.jaquarius.annotation.Properties;
import intellispaces.ixora.rdb.guide.IxoraParameterizedQueryToBlindQueryGuide;
import intellispaces.ixora.rdb.guide.IxoraResultSetToDataGuide;

@Configuration({
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
