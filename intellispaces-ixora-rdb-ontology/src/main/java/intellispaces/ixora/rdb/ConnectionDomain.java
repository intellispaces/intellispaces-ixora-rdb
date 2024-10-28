package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Domain;
import intellispaces.jaquarius.annotation.Movable;
import intellispaces.jaquarius.traverse.TraverseTypes;

@Domain("f72b8a37-6f1e-4c98-93b5-9d5bb959cb80")
public interface ConnectionDomain {

  @Channel(
    value = "b7c73781-0441-4ef7-b5b3-f122b5bccd29",
    name = "ConnectionToStatementChannel",
    allowedTraverse = TraverseTypes.MappingOfMoving
  )
  @Movable
  StatementDomain createStatement();

  @Channel(
    value = "4a08c0f9-0159-4b69-9211-3ec1d8a6200c",
    name = "ConnectionToPreparedStatementChannel",
    allowedTraverse = TraverseTypes.Mapping
  )
  @Movable
  PreparedStatementDomain createPreparedStatement(String query);

  @Channel(value = "0f840bdd-29eb-4847-9209-199f13de78a8", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain disableAutoCommit();

  @Channel(value = "7e5ce719-8cb6-452f-a1e2-a0f08fc786a3", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain commit();

  @Channel(value = "f2b08c59-c52e-4915-9d14-ac6352e2fe23", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain rollback();

  @Channel(value = "33512973-f011-4ab8-81b9-0cdf4ba7b082", allowedTraverse = TraverseTypes.Moving)
  ConnectionDomain close();
}
