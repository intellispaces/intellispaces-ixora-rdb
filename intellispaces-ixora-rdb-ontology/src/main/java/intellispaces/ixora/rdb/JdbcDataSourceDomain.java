package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Domain;

@Domain("01908c4e-41be-7ec5-a13f-296a45f141ae")
public interface JdbcDataSourceDomain extends DataSourceDomain {

  @Channel("3abadcb1-98c3-4e66-8aba-34268df74883")
  DataSourceDomain asDataSource();
}
