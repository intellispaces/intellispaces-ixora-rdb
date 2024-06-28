package tech.intellispaces.ixora.rdb.unit;

import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Properties;
import tech.intellispaces.framework.core.annotation.Unit;
import tech.intellispaces.ixora.rdb.BasicTransactionFactoryImpl;
import tech.intellispaces.ixora.rdb.DataSourceMovableHandle;
import tech.intellispaces.ixora.rdb.DataSourcePropertiesHandle;
import tech.intellispaces.ixora.rdb.TransactionFactoryMovableHandle;

@Unit
public abstract class RdbUnit {

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
    return new BasicTransactionFactoryImpl(dataSource);
  }
}
