package tech.intellispaces.ixora.rdb.annitation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@Around()
//@Inject(value = "tx", type = Transaction.class)
public @interface Transactional {
}
