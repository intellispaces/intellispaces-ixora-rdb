package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Factory;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.traverse.TraverseTypes;

@Domain("f72b8a37-6f1e-4c98-93b5-9d5bb959cb80")
public interface ConnectionDomain {

  @Factory
  @Transition(
    value = "b7c73781-0441-4ef7-b5b3-f122b5bccd29",
    name = "ConnectionToStatementTransition",
    allowedTraverse = TraverseTypes.MappingRelatedToMoving
  )
  StatementDomain createStatement();

  @Factory
  @Transition(
    value = "4a08c0f9-0159-4b69-9211-3ec1d8a6200c",
    name = "ConnectionToPreparedStatementTransition",
    allowedTraverse = TraverseTypes.Mapping
  )
  PreparedStatementDomain createPreparedStatement(String query);

  @Transition(value = "0f840bdd-29eb-4847-9209-199f13de78a8", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain disableAutoCommit();

  @Transition(value = "7e5ce719-8cb6-452f-a1e2-a0f08fc786a3", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain commit();

  @Transition(value = "f2b08c59-c52e-4915-9d14-ac6352e2fe23", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain rollback();

  @Transition(value = "33512973-f011-4ab8-81b9-0cdf4ba7b082", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain close();
}
