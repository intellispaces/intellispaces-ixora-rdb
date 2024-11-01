package intellispaces.ixora.rdb.aop;

import intellispaces.common.action.Action;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.aop.Interceptor;
import intellispaces.jaquarius.system.ProjectionProvider;
import intellispaces.ixora.rdb.MovableTransactionFactory;
import intellispaces.ixora.rdb.TransactionFunctions;
import intellispaces.ixora.rdb.exception.TransactionException;

import java.util.List;

public class TransactionalInterceptor extends Interceptor {

  public TransactionalInterceptor(
      MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider
  ) {
    super(joinPoint, nextAction, projectionProvider);
  }

  @Override
  public Object execute(Object[] data) {
    MovableTransactionFactory transactionFactory = getDefaultTransactionFactory();
    Action joinAction = joinAction();
    return TransactionFunctions.transactional(transactionFactory, joinAction::execute, data);
  }

  private MovableTransactionFactory getDefaultTransactionFactory() {
    List<MovableTransactionFactory> transactionFactories = projectionProvider.getProjections(MovableTransactionFactory.class);
    if (transactionFactories.isEmpty()) {
      throw TransactionException.withMessage("Transaction factory is not found");
    }
    if (transactionFactories.size() > 1) {
      throw TransactionException.withMessage("Multiple transaction factories are found");
    }
    return transactionFactories.get(0);
  }
}
