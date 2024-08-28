package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.ixora.rdb.annotation.Entity;
import intellispaces.javastatements.customtype.CustomType;

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
  public List<ArtifactGenerator> makeArtifactGenerators(CustomType entityType, RoundEnvironment roundEnv) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new EntityCrudOntologyGenerator(entityType));
    generators.add(new EntityCrudGuideGenerator(entityType));
    generators.add(new EntityCrudGuideImplGenerator(entityType));
    return generators;
  }
}
