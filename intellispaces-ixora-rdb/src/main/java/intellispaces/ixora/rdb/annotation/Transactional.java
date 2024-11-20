package intellispaces.ixora.rdb.annotation;

import intellispaces.ixora.rdb.aop.TransactionalInterceptor;
import intellispaces.jaquarius.annotation.ApplyAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApplyAdvice(adviceClass = TransactionalInterceptor.class)
public @interface Transactional {
}
