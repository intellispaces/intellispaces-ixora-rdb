package tech.intellispaces.ixora.rdb;

import tech.intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;

@Domain("1931adca-ed62-4b0f-af4f-6d7dee9b7822")
public interface TransactionFactoryDomain {

  @Channel("42c35449-eb32-4683-ba40-9ded4a1d38f4")
  DataSourceDomain dataSource();

  @Channel(
    value = "03e420fb-2cf3-49ea-a1f3-8adff39e738b",
    name = "TransactionFactoryToTransactionChannel",
    allowedTraverse = TraverseTypes.MappingOfMoving
  )
  @Movable
  TransactionDomain getTransaction() throws TransactionException;
}
