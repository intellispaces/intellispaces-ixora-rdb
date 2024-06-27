package tech.intellispaces.ixora.rdb;

import tech.intellispaces.framework.core.annotation.ObjectHandle;

@ObjectHandle
public abstract class JdbcDataSource implements DataSourceMovableHandle {
  private final DataSourcePropertiesHandle dataSourcePropertiesHandle;

  public JdbcDataSource(DataSourcePropertiesHandle dataSourcePropertiesHandle) {
    this.dataSourcePropertiesHandle = dataSourcePropertiesHandle;
  }

  @Override
  public DataSourcePropertiesHandle properties() {
    return dataSourcePropertiesHandle;
  }

  @Override
  public ConnectionHandle getConnection() {



    return null;
  }
}
