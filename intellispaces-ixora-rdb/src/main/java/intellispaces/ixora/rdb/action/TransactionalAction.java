package intellispaces.ixora.rdb.action;

import intellispaces.ixora.rdb.MovableTransactionFactory;
import intellispaces.ixora.rdb.TransactionFunctions;
import tech.intellispaces.action.Action;
import tech.intellispaces.action.wrapper.AbstractWrapperAction;

public class TransactionalAction extends AbstractWrapperAction {
  private final MovableTransactionFactory transactionFactory;

  public TransactionalAction(MovableTransactionFactory transactionFactory, Action wrappedAction) {
    super(wrappedAction);
    this.transactionFactory = transactionFactory;
  }

  @Override
  public Object execute(Object... data) {
    return TransactionFunctions.transactional(transactionFactory, wrappedAction()::execute, data);
  }
}
