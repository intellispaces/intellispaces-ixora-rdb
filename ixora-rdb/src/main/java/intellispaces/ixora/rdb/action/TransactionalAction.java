package intellispaces.ixora.rdb.action;

import intellispaces.ixora.rdb.TransactionFactory;
import intellispaces.ixora.rdb.TransactionFunctions;
import intellispaces.actions.Action;
import intellispaces.actions.wrapper.AbstractWrapper;

public class TransactionalAction extends AbstractWrapper {
  private final TransactionFactory transactionFactory;

  public TransactionalAction(TransactionFactory transactionFactory, Action wrappedAction) {
    super(wrappedAction);
    this.transactionFactory = transactionFactory;
  }

  @Override
  public Object execute(Object... data) {
    return TransactionFunctions.transactional(transactionFactory, wrappedAction()::execute, data);
  }
}
