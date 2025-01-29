package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.guide.IxoraParameterizedQueryToBlindQueryGuide;
import tech.intellispaces.ixora.rdb.guide.IxoraResultSetToDataGuide;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.Settings;

@Configuration({
    IxoraParameterizedQueryToBlindQueryGuide.class,
    IxoraResultSetToDataGuide.class
})
public abstract class RdbConfiguration {

  /**
   * Data source properties.
   */
  @Projection
  @Settings("datasource")
  public abstract DataSourceSettingsHandle dataSourceSettings();

  /**
   * Transaction factory.
   */
  @Projection
  public MovableTransactionFactoryHandle transactionFactory(MovableDataSourceHandle dataSource) {
    return new TransactionFactoryHandleOverDataSourceImpl(dataSource);
  }
}
