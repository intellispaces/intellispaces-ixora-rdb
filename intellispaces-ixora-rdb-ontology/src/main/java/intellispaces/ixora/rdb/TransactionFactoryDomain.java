package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Factory;
import intellispaces.framework.core.annotation.TargetSpecification;
import intellispaces.framework.core.traverse.TraverseTypes;
import intellispaces.ixora.rdb.exception.TransactionException;

@Domain("1931adca-ed62-4b0f-af4f-6d7dee9b7822")
public interface TransactionFactoryDomain {

  @Channel("42c35449-eb32-4683-ba40-9ded4a1d38f4")
  DataSourceDomain dataSource();

  @Factory
  @Channel(
    value = "03e420fb-2cf3-49ea-a1f3-8adff39e738b",
    name = "TransactionFactoryToTransactionChannel",
    allowedTraverse = TraverseTypes.MappingOfMoving,
    targetSpecifications = TargetSpecification.Movable
  )
  TransactionDomain getTransaction() throws TransactionException;
}
