package tech.intellispaces.ixora.rdb;

import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

@Domain("8a1c0f79-78f2-4757-8f30-8a0fdeea4b93")
public interface DataSourceDomain {

  @Channel("ea808587-a507-40e1-9091-0c623dcb6d74")
  DataSourceSettingsDomain properties();

  @Channel(
      value = "7465cd7c-94bd-4aac-b220-1520907c4e9b",
      name = "DataSourceToConnectionChannel",
      allowedTraverse = TraverseTypes.MappingOfMoving
  )
  @Movable
  ConnectionDomain getConnection();
}
