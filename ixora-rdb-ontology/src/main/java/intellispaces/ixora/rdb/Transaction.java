package intellispaces.ixora.rdb;

import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.traverse.TraverseTypes;
import intellispaces.ixora.structures.collection.Cursor;

@Domain("8f174862-2fab-48cb-af12-b8e264f19257")
public interface Transaction {

  @Transition("038c69e9-7231-49e1-9a71-156018ea026b")
  Connection connection();

  @Transition(value = "8f3720d1-f451-41c0-bac6-d9d1c4c11448", allowedTraverseTypes = TraverseTypes.Moving)
  Transaction commit();

  @Transition(value = "68ea2724-07be-463f-8f61-b9102a91efea", allowedTraverseTypes = TraverseTypes.Moving)
  Transaction rollback();

  @Transition(value = "5dce771b-2908-444b-ba15-6c0b2167fe33", allowedTraverseTypes = TraverseTypes.Moving)
  Transaction update(String sql);

  @Transition(value = "580e6c95-881e-47f3-a43e-bce3dd2c628d", name = "TransactionQueryTransition")
  ResultSet query(String sql);

  @Transition(value = "562a0437-6e55-492b-ac35-70ca1ddf57f0", name = "TransactionQueryDataTransition")
  <T> Cursor<T> queryData(Class<T> dataType, String sql);

  @Transition(value = "7490e2fd-b136-4afa-9fea-d1db7acc1864", name = "TransactionFetchDataTransition")
  <T> T fetchData(Class<T> type, String sql);
}
