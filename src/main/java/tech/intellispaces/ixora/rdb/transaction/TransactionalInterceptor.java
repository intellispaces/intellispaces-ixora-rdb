package tech.intellispaces.ixora.rdb.transaction;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import intellispaces.ixora.rdb.exception.TransactionException;
import tech.intellispaces.actions.Action;
import tech.intellispaces.core.aop.InterceptorAdvice;
import tech.intellispaces.core.system.Modules;

import java.util.List;

public class TransactionalInterceptor extends InterceptorAdvice {

  @Override
  public Object execute(Object[] data) {
    TransactionFactoryHandle transactionFactory = getDefaultTransactionFactory();
    Action interceptedAction = interceptedAction();
    return TransactionFunctions.transactional(transactionFactory, interceptedAction::execute, data);
  }

  private TransactionFactoryHandle getDefaultTransactionFactory() {
    List<TransactionFactoryHandle> transactionFactories = Modules.current()
        .getProjections(TransactionFactoryHandle.class);
    if (transactionFactories.isEmpty()) {
      throw TransactionException.withMessage("Transaction factory is not found");
    }
    if (transactionFactories.size() > 1) {
      throw TransactionException.withMessage("Multiple transaction factories are found");
    }
    return transactionFactories.get(0);
  }
}
