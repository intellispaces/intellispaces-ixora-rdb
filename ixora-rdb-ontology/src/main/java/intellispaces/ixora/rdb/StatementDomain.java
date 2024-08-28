package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.Transition;
import intellispaces.core.traverse.TraverseTypes;

@Domain("24c32319-367b-4d90-8ff0-307532d7ac0d")
public interface StatementDomain {

  @Transition(value = "b9f8a9d2-cb25-4db2-a94a-7053dd375e3c", allowedTraverse = TraverseTypes.Moving)
  ResultSetDomain executeQuery(String sql);
}
