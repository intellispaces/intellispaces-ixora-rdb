package tech.intellispaces.ixora.rdb.aop;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.ixora.rdb.TransactionFunctions;
import tech.intellispaces.ixora.rdb.exception.TransactionExceptions;
import tech.intellispaces.jaquarius.aop.Interceptor;
import tech.intellispaces.jaquarius.ixora.rdb.MovableTransactionFactoryHandle;
import tech.intellispaces.jaquarius.system.ProjectionProvider;

import java.util.List;

public class TransactionalInterceptor extends Interceptor {

  public TransactionalInterceptor(
      MethodStatement joinPoint, Action nextAction, ProjectionProvider projectionProvider
  ) {
    super(joinPoint, nextAction, projectionProvider);
  }

  @Override
  public int order() {
    return wrappedAction().order();
  }

  @Override
  public Object execute(Object[] data) {
    MovableTransactionFactoryHandle transactionFactory = getDefaultTransactionFactory();
    Action joinAction = joinAction();
    return TransactionFunctions.transactional(transactionFactory, joinAction::execute, data);
  }

  private MovableTransactionFactoryHandle getDefaultTransactionFactory() {
    List<MovableTransactionFactoryHandle> transactionFactories = projectionProvider.getProjections(MovableTransactionFactoryHandle.class);
    if (transactionFactories.isEmpty()) {
      throw TransactionExceptions.withMessage("Transaction factory is not found");
    }
    if (transactionFactories.size() > 1) {
      throw TransactionExceptions.withMessage("Multiple transaction factories are found");
    }
    return transactionFactories.get(0);
  }
}
