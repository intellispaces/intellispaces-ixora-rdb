package intellispaces.ixora.rdb;

import tech.intellispaces.core.annotation.Data;
import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.Transition;

@Data
@Domain("a0d2d709-9af1-4bcc-8a90-c3c828585198")
public interface DataSourceProperties {

  @Transition("04006c73-1ca9-46f3-9a5a-f8e5e1ab59e7")
  String driverClassName();

  @Transition("c35bb6bf-8e65-49df-86aa-4c07faee4d6a")
  String url();

  @Transition("eac366a8-f629-4589-a9a4-a30ee3abec8d")
  String username();

  @Transition("b254be32-7b81-4092-b821-4803c3af770d")
  String password();
}
