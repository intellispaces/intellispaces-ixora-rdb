package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.traverse.TraverseTypes;
import intellispaces.ixora.structures.association.MapDomain;
import intellispaces.ixora.structures.collection.CursorDomain;

@Domain("8f174862-2fab-48cb-af12-b8e264f19257")
public interface TransactionDomain {

  @Transition("038c69e9-7231-49e1-9a71-156018ea026b")
  ConnectionDomain connection();

  @Transition(value = "8f3720d1-f451-41c0-bac6-d9d1c4c11448", allowedTraverse = TraverseTypes.Moving)
  TransactionDomain commit();

  @Transition(value = "68ea2724-07be-463f-8f61-b9102a91efea", allowedTraverse = TraverseTypes.Moving)
  TransactionDomain rollback();

  @Transition(value = "5dce771b-2908-444b-ba15-6c0b2167fe33", allowedTraverse = TraverseTypes.Moving)
  TransactionDomain modify(String query);

  @Transition(value = "580e6c95-881e-47f3-a43e-bce3dd2c628d", name = "TransactionQueryTransition")
  ResultSetDomain query(String query);

  @Transition(value = "562a0437-6e55-492b-ac35-70ca1ddf57f0", name = "TransactionQueryDataTransition")
  <D> CursorDomain<D> queryData(Class<D> dataType, String sql);

  @Transition(value = "7490e2fd-b136-4afa-9fea-d1db7acc1864", name = "TransactionFetchDataTransition")
  <D> D fetchData(Class<D> dataType, String query);

  @Transition(value = "3b3bcd62-0675-459a-990b-46b87860abbc", name = "TransactionFetchDataParameterizedTransition")
  <D> D fetchData(Class<D> dataType, String query, MapDomain<String, Object> params);
}
