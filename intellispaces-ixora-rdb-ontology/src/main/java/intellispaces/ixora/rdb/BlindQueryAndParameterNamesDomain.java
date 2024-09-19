package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.ixora.structures.collection.ListDomain;

@Data
@Domain("db6fbe62-df10-41d8-8192-f3324af11ef4")
public interface BlindQueryAndParameterNamesDomain {

  @Transition("6cd3d0ac-72d5-4f5a-bb15-e7d8eb4eeefe")
  String query();

  @Transition("643c6816-45d7-4d9a-b09a-3f6fcb32195c")
  ListDomain<String> parameterNames();
}
