package tech.intellispaces.ixora.rdb.action;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.wrapper.AbstractWrapper;
import tech.intellispaces.ixora.rdb.TransactionFunctions;

public class TransactionalAction extends AbstractWrapper {
  private final TransactionFactoryHandle transactionFactory;

  public TransactionalAction(TransactionFactoryHandle transactionFactory, Action wrappedAction) {
    super(wrappedAction);
    this.transactionFactory = transactionFactory;
  }

  @Override
  public Object execute(Object... data) {
    return TransactionFunctions.transactional(transactionFactory, wrappedAction()::execute, data);
  }
}
