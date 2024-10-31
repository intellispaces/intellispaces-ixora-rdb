package intellispaces.ixora.rdb.processor.entity;

import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.ixora.rdb.annotation.Entity;

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
