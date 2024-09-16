package intellispaces.ixora.rdb.action;

import intellispaces.common.action.Action;
import intellispaces.common.action.wrapper.AbstractWrapper;
import intellispaces.ixora.rdb.TransactionFactory;
import intellispaces.ixora.rdb.TransactionFunctions;

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
