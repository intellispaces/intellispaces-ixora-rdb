package intellispaces.ixora.rdb.annotation;

import intellispaces.jaquarius.annotation.AnnotationProcessor;
import intellispaces.jaquarius.annotation.Data;
import intellispaces.ixora.rdb.annotation.processor.entity.EntityAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Data
@AnnotationProcessor(EntityAnnotationProcessor.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
}
