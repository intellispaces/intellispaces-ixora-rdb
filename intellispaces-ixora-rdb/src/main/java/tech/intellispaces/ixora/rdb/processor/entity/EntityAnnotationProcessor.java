package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.ixora.rdb.annotation.Entity;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EntityAnnotationProcessor extends AnnotatedTypeProcessor {

  public EntityAnnotationProcessor() {
    super(Entity.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType entityType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(entityType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType entityType, RoundEnvironment roundEnv) {
    List<Generator> generators = new ArrayList<>();
    generators.add(new EntityCrudOntologyGenerator(initiatorType, entityType));
    generators.add(new EntityCrudGuideGenerator(initiatorType, entityType));
    generators.add(new EntityCrudGuideImplGenerator(initiatorType, entityType));
    return generators;
  }
}
