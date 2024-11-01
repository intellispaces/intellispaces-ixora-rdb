package intellispaces.ixora.rdb;

import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Domain;
import intellispaces.jaquarius.annotation.Movable;
import intellispaces.jaquarius.traverse.TraverseTypes;

@Domain("d3660036-a556-4ae1-a322-4a60d4c3b2e2")
public interface PreparedStatementDomain {

  @Channel(
      value = "b1d7a815-567d-4b0d-a252-1a769212fb5b",
      allowedTraverse = TraverseTypes.Mapping
  )
  @Movable
  ResultSetDomain executeQuery();

  @Channel(value = "6edca404-da35-4b0f-b249-08964c685e81", allowedTraverse = TraverseTypes.Moving)
  PreparedStatementDomain setInt(int parameterIndex, int value);
}
