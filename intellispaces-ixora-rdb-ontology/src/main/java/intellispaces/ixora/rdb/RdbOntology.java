package intellispaces.ixora.rdb;

import intellispaces.ixora.data.collection.ListDomain;
import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Ontology;
import intellispaces.jaquarius.traverse.TraverseTypes;

@Ontology
public interface RdbOntology {

  @Channel("d3673390-b743-434a-b7c9-f7dc64976387")
  BlindQueryAndParameterNamesDomain parameterizedQueryToBlindQuery(String query);

  @Channel("d280d0b1-16d9-4d3a-a172-a0ad39c63de7")
  <D> D resultSetToData(ResultSetDomain resultSet, Class<D> dataClass);

  @Channel(value = "befbf6f7-50af-491f-a17c-574723315122", allowedTraverse = TraverseTypes.MappingOfMoving)
  <D> ListDomain<D> resultSetToDataList(ResultSetDomain resultSet, Class<D> dataClass);
}
