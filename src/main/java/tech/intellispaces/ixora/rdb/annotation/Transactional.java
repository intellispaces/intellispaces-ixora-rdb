package tech.intellispaces.ixora.rdb.annotation;

import tech.intellispaces.core.annotation.ApplyAdvice;
import tech.intellispaces.ixora.rdb.transaction.TransactionalAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApplyAdvice(adviceClass = TransactionalAction.class)
public @interface Transactional {
}
