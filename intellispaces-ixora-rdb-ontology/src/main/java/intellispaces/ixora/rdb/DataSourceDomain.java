package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Factory;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.traverse.TraverseTypes;

@Factory
@Domain("8a1c0f79-78f2-4757-8f30-8a0fdeea4b93")
public interface DataSourceDomain {

  @Transition("ea808587-a507-40e1-9091-0c623dcb6d74")
  DataSourcePropertiesDomain properties();

  @Transition(
      value = "7465cd7c-94bd-4aac-b220-1520907c4e9b",
      name = "DataSourceToConnectionTransition",
      allowedTraverse = TraverseTypes.Moving,
      factory = true
  )
  ConnectionDomain getConnection();
}
