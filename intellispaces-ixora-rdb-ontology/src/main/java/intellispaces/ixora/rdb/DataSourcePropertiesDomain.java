package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Data;
import intellispaces.jaquarius.annotation.Domain;

@Data
@Domain("a0d2d709-9af1-4bcc-8a90-c3c828585198")
public interface DataSourcePropertiesDomain {

  @Channel("04006c73-1ca9-46f3-9a5a-f8e5e1ab59e7")
  String driverClassName();

  @Channel("c35bb6bf-8e65-49df-86aa-4c07faee4d6a")
  String url();

  @Channel("eac366a8-f629-4589-a9a4-a30ee3abec8d")
  String username();

  @Channel("b254be32-7b81-4092-b821-4803c3af770d")
  String password();
}
