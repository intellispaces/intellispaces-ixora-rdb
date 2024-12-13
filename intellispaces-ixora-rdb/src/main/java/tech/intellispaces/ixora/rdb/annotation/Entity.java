package tech.intellispaces.ixora.rdb.annotation;

import tech.intellispaces.ixora.rdb.processor.entity.EntityProcessor;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;
import tech.intellispaces.jaquarius.annotation.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Data
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotationProcessor(EntityProcessor.class)
public @interface Entity {
}
