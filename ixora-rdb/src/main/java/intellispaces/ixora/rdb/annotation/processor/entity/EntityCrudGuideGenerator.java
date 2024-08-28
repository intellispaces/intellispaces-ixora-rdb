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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideGenerator extends AbstractGenerator {
  private boolean entityHasIdentifier;
  private String entityHandleSimpleName;
  private String identifierType;
  private String identifierToEntityTransitionSimpleName;
  private String transactionToEntityByIdentifierTransitionSimpleName;

  public EntityCrudGuideGenerator(CustomType entityType) {
    super(entityType);
  }

  @Override
  public String getArtifactName() {
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
    context.generatedClassCanonicalName(getArtifactName());
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
