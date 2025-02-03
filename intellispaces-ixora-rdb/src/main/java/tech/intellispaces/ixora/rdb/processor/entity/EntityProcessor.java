package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.ixora.rdb.annotation.Entity;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;

public class EntityProcessor extends ArtifactProcessor {

  public EntityProcessor() {
    super(ElementKind.INTERFACE, Entity.class, JaquariusArtifactProcessor.SOURCE_VERSION);
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
