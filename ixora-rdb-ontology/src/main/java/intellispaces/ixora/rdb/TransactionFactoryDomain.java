package intellispaces.ixora.rdb;

import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.Factory;
import intellispaces.core.annotation.Transition;
import intellispaces.core.traverse.TraverseTypes;
import intellispaces.ixora.rdb.exception.TransactionException;

@Factory
@Domain("1931adca-ed62-4b0f-af4f-6d7dee9b7822")
public interface TransactionFactoryDomain {

  @Transition("42c35449-eb32-4683-ba40-9ded4a1d38f4")
  DataSourceDomain dataSource();

  @Transition(value = "03e420fb-2cf3-49ea-a1f3-8adff39e738b", allowedTraverseTypes = TraverseTypes.Moving, factory = true)
  TransactionDomain getTransaction() throws TransactionException;
}
