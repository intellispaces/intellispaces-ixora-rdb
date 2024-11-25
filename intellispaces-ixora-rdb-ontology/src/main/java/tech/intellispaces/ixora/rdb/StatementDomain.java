package tech.intellispaces.ixora.rdb;

import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

@Domain("24c32319-367b-4d90-8ff0-307532d7ac0d")
public interface StatementDomain {

  @Channel(
      value = "b9f8a9d2-cb25-4db2-a94a-7053dd375e3c",
      allowedTraverse = TraverseTypes.Mapping
  )
  @Movable
  ResultSetDomain executeQuery(String query);
}
