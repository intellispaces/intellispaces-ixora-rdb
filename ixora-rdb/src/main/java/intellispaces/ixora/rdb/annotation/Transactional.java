package intellispaces.ixora.rdb.annotation;

import intellispaces.core.annotation.ApplyAdvice;
import intellispaces.ixora.rdb.aop.TransactionalInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApplyAdvice(adviceClass = TransactionalInterceptor.class)
public @interface Transactional {
}
