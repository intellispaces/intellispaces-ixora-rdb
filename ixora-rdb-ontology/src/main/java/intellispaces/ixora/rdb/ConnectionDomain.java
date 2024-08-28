package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.Factory;
import intellispaces.core.annotation.Transition;
import intellispaces.core.traverse.TraverseTypes;

@Factory
@Domain("f72b8a37-6f1e-4c98-93b5-9d5bb959cb80")
public interface ConnectionDomain {

  @Transition(value = "b7c73781-0441-4ef7-b5b3-f122b5bccd29", allowedTraverse = TraverseTypes.Moving, factory = true)
  StatementDomain createStatement();

  @Transition(value = "33512973-f011-4ab8-81b9-0cdf4ba7b082", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain close();
}
