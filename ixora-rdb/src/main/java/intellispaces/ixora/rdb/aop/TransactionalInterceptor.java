package intellispaces.ixora.rdb.aop;

import intellispaces.ixora.rdb.TransactionFactoryHandle;
import intellispaces.ixora.rdb.TransactionFunctions;
import intellispaces.ixora.rdb.exception.TransactionException;
import intellispaces.actions.Action;
import intellispaces.core.aop.Interceptor;
import intellispaces.core.system.Modules;
import intellispaces.javastatements.method.MethodStatement;

import java.util.List;

public class TransactionalInterceptor extends Interceptor {

  public TransactionalInterceptor(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }

  @Override
  public Object execute(Object[] data) {
    TransactionFactoryHandle transactionFactory = getDefaultTransactionFactory();
    Action joinAction = joinAction();
    return TransactionFunctions.transactional(transactionFactory, joinAction::execute, data);
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
