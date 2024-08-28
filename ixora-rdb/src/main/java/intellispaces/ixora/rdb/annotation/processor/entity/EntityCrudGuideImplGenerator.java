package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.core.annotation.Guide;
import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.processor.AbstractGenerator;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.ixora.rdb.Transaction;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideImplGenerator extends AbstractGenerator {
  private String entityHandleSimpleName;
  private boolean entityHasIdentifier;
  private String identifierType;
  private String guideType;

  public EntityCrudGuideImplGenerator(CustomType entityType) {
    super(entityType);
  }

  @Override
  public String getArtifactName() {
    return EntityProcessorFunctions.getCrudGuideGeneratedImplCanonicalName(annotatedType);
  }

  @Override
  protected String templateName() {
    return "/entity_crud_guide_impl.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", makeGeneratedAnnotation(),
        "packageName", context.packageName(),
        "sourceCanonicalName", sourceClassCanonicalName(),
        "sourceSimpleName", sourceClassSimpleName(),
        "classSimpleName", context.generatedClassSimpleName(),
        "importedClasses", context.getImports(),
        "guideType", guideType,
        "entityHandleSimpleName", entityHandleSimpleName,
        "entityHasIdentifier", entityHasIdentifier,
        "identifierType", identifierType
    );
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());

    context.addImport(Guide.class);
    context.addImport(Transaction.class);

    guideType = context.addToImportAndGetSimpleName(EntityProcessorFunctions.getCrudGuideCanonicalName(annotatedType));
    entityHandleSimpleName = context.addToImportAndGetSimpleName(
        EntityProcessorFunctions.getEntityHandleCanonicalName(annotatedType)
    );
    analyzeEntityIdentifier();
    return true;
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityProcessorFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierType = context.addToImportAndGetSimpleName(
        EntityProcessorFunctions.getIdentifierType(annotatedType, identifierMethod.orElseThrow())
    );
  }
}
