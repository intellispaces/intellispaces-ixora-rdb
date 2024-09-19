package intellispaces.ixora.rdb.aop;

import intellispaces.common.action.Action;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.framework.core.aop.Interceptor;
import intellispaces.framework.core.system.Modules;
import intellispaces.ixora.rdb.TransactionFactory;
import intellispaces.ixora.rdb.TransactionFunctions;
import intellispaces.ixora.rdb.exception.TransactionException;

import java.util.List;

public class TransactionalInterceptor extends Interceptor {

  public TransactionalInterceptor(MethodStatement joinPoint, Action nextAction) {
    super(joinPoint, nextAction);
  }

  @Override
  public Object execute(Object[] data) {
    TransactionFactory transactionFactory = getDefaultTransactionFactory();
    Action joinAction = joinAction();
    return TransactionFunctions.transactional(transactionFactory, joinAction::execute, data);
  }

  private TransactionFactory getDefaultTransactionFactory() {
    List<TransactionFactory> transactionFactories = Modules.current()
        .getProjections(TransactionFactory.class);
    if (transactionFactories.isEmpty()) {
      throw TransactionException.withMessage("Transaction factory is not found");
    }
    if (transactionFactories.size() > 1) {
      throw TransactionException.withMessage("Multiple transaction factories are found");
    }
    return transactionFactories.get(0);
  }
}
