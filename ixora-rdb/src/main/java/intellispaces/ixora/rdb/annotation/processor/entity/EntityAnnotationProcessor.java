package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.GenerationTask;
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
  public List<GenerationTask> makeTasks(CustomType initiatorType, CustomType entityType, RoundEnvironment roundEnv) {
    List<GenerationTask> tasks = new ArrayList<>();
    tasks.add(new EntityCrudOntologyGenerationTask(initiatorType, entityType));
    tasks.add(new EntityCrudGuideGenerationTask(initiatorType, entityType));
    tasks.add(new EntityCrudGuideImplGenerationTask(initiatorType, entityType));
    return tasks;
  }
}
