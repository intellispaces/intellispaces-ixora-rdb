package tech.intellispaces.ixora.rdb.action;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import tech.intellispaces.actions.AbstractAction;
import tech.intellispaces.actions.Action;
import tech.intellispaces.ixora.rdb.TransactionFunctions;

public class TransactionalAction extends AbstractAction {
  private final TransactionFactoryHandle transactionFactory;
  private final Action internalAction;

  public TransactionalAction(TransactionFactoryHandle transactionFactory, Action internalAction) {
    this.transactionFactory = transactionFactory;
    this.internalAction = internalAction;
  }

  @Override
  public Object execute(Object... data) {
    return TransactionFunctions.transactional(transactionFactory, internalAction::execute, data);
  }
}
