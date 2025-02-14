package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.guide.StringToParameterizedNamedQueryGuideImpl;
import tech.intellispaces.ixora.rdb.guide.IxoraResultSetToDataGuide;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.Settings;
import tech.intellispaces.jaquarius.ixora.rdb.DataSourceSettingsHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableDataSourceHandle;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionFactoryHandle;

@Configuration({
    StringToParameterizedNamedQueryGuideImpl.class,
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
    return new TransactionFactoryHandleOverDataSourceWrapper(dataSource);
  }
}
