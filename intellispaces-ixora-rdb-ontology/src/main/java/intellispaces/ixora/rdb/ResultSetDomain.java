package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.traverse.TraverseTypes;
import intellispaces.ixora.data.collection.ListDomain;

@Domain("80898b0a-6a68-4693-bc86-17d7c5bb6a64")
public interface ResultSetDomain {

  @Channel(value = "a0b049ef-9ff2-4611-95b4-b500673afa55", allowedTraverse = TraverseTypes.MappingOfMoving)
  Boolean next();

  @Channel(value = "77aeebe4-6c9f-4067-a1be-fe7633558a13", name = "ResultSetToIntegerValueByNameChannel")
  Integer integerValue(String name);

  @Channel(value = "0fb39956-45e1-4b78-afdf-aa0b4922ed86", name = "ResultSetToStringValueByNameChannel")
  String stringValue(String name);

  @Channel(value = "d280d0b1-16d9-4d3a-a172-a0ad39c63de7", name = "ResultSetToDataChannel")
  <D> D dataValue(Class<D> dataClass);

  @Channel(
      value = "befbf6f7-50af-491f-a17c-574723315122",
      name = "ResultSetToDataListChannel",
      allowedTraverse = TraverseTypes.MappingOfMoving)
  <D> ListDomain<D> dataValues(Class<D> dataClass);
}
