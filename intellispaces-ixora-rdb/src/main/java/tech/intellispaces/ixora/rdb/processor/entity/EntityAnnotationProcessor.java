package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.ixora.rdb.annotation.Entity;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;

public class EntityAnnotationProcessor extends ArtifactProcessor {

  public EntityAnnotationProcessor() {
    super(ElementKind.INTERFACE, Entity.class, AnnotationProcessorConstants.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType entityType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(entityType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType entityType, ArtifactGeneratorContext context) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    generators.add(new EntityCrudOntologyGenerator(entityType));
    generators.add(new EntityCrudGuideGenerator(entityType));
    generators.add(new EntityCrudGuideImplGenerator(entityType));
    return generators;
  }
}
