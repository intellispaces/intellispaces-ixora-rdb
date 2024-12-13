package tech.intellispaces.ixora.rdb.action;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.wrapper.AbstractWrapperAction;
import tech.intellispaces.ixora.rdb.MovableTransactionFactory;
import tech.intellispaces.ixora.rdb.TransactionFunctions;

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
