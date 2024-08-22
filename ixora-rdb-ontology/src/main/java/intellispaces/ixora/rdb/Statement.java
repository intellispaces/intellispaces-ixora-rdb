package intellispaces.ixora.rdb;

import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.traverse.TraverseTypes;

@Domain("24c32319-367b-4d90-8ff0-307532d7ac0d")
public interface Statement {

  @Transition(value = "b9f8a9d2-cb25-4db2-a94a-7053dd375e3c", allowedTraverseTypes = TraverseTypes.Moving)
  ResultSet executeQuery(String sql);
}
