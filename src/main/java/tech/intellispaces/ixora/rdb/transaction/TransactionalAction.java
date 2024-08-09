package tech.intellispaces.ixora.rdb.transaction;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import tech.intellispaces.actions.Action;
import tech.intellispaces.core.aop.InterceptorAdvice;

public class TransactionalAction extends InterceptorAdvice {

  @Override
  public Object execute(Object[] data) {
    TransactionFactoryHandle transactionFactory = null;


    Action interceptedAction = interceptedAction();
    Object result = TransactionFunctions.transactional(
        transactionFactory, interceptedAction::execute, data
    );


    return result;
  }
}
