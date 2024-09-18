package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.traverse.TraverseTypes;

@Domain("24c32319-367b-4d90-8ff0-307532d7ac0d")
public interface StatementDomain {

  @Transition(value = "b9f8a9d2-cb25-4db2-a94a-7053dd375e3c", allowedTraverse = TraverseTypes.Mapping)
  ResultSetDomain executeQuery(String query);
}
