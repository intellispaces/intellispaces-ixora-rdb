package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.data.collection.ListDomain;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotation.Domain;

@Data
@Domain("db6fbe62-df10-41d8-8192-f3324af11ef4")
public interface BlindQueryAndParameterNamesDomain {

  @Channel("6cd3d0ac-72d5-4f5a-bb15-e7d8eb4eeefe")
  String blindQuery();

  @Channel("643c6816-45d7-4d9a-b09a-3f6fcb32195c")
  ListDomain<String> parameterNames();
}
