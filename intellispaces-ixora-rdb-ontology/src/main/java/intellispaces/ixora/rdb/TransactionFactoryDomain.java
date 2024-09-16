package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Factory;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.traverse.TraverseTypes;
import intellispaces.ixora.rdb.exception.TransactionException;

@Factory
@Domain("1931adca-ed62-4b0f-af4f-6d7dee9b7822")
public interface TransactionFactoryDomain {

  @Transition("42c35449-eb32-4683-ba40-9ded4a1d38f4")
  DataSourceDomain dataSource();

  @Transition(value = "03e420fb-2cf3-49ea-a1f3-8adff39e738b", allowedTraverse = TraverseTypes.Moving, factory = true)
  TransactionDomain getTransaction() throws TransactionException;
}
