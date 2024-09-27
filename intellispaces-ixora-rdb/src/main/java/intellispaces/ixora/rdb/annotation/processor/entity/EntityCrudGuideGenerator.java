package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.processor.AbstractGenerator;
import intellispaces.ixora.rdb.Transaction;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideGenerator extends AbstractGenerator {
  private boolean entityHasIdentifier;
  private String entityHandleSimpleName;
  private String identifierType;
  private String identifierToEntityTransitionSimpleName;
  private String transactionToEntityByIdentifierTransitionSimpleName;

  public EntityCrudGuideGenerator(CustomType initiatorType, CustomType entityType) {
    super(initiatorType, entityType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return context.isProcessingFinished(
        Ontology.class, EntityProcessorFunctions.getCrudOntologyCanonicalName(annotatedType)
    );
  }

  @Override
  public String artifactName() {
    return EntityProcessorFunctions.getCrudGuideCanonicalName(annotatedType);
  }

  @Override
  protected String templateName() {
    return "/entity_crud_guide.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceCanonicalName", sourceClassCanonicalName());
    vars.put("sourceSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("importedClasses", context.getImports());

    vars.put("entityHasIdentifier", entityHasIdentifier);
    vars.put("entityHandleSimpleName", entityHandleSimpleName);
    vars.put("identifierType", identifierType);
    vars.put("identifierToEntityTransitionSimpleName", identifierToEntityTransitionSimpleName);
    vars.put("transactionToEntityByIdentifierTransitionSimpleName", transactionToEntityByIdentifierTransitionSimpleName);

    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    context.addImport(Guide.class);
    context.addImport(Mapper.class);
    context.addImport(Transaction.class);

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
    identifierToEntityTransitionSimpleName = EntityProcessorFunctions.getIdentifierToEntityTransitionSimpleName(
        annotatedType);
    transactionToEntityByIdentifierTransitionSimpleName = EntityProcessorFunctions.getTransactionToEntityByIdentifierTransitionSimpleName(
        annotatedType
    );
  }
}
