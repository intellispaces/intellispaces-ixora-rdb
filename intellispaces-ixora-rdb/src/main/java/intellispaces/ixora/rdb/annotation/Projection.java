package intellispaces.ixora.rdb.annotation;

import intellispaces.framework.core.annotation.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Data
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Projection {
}
