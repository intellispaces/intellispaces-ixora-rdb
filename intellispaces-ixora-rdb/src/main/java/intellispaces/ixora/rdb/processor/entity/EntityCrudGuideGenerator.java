package intellispaces.ixora.rdb.processor.entity;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.annotation.Guide;
import intellispaces.jaquarius.annotation.Mapper;
import intellispaces.jaquarius.annotation.Ontology;
import intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import intellispaces.ixora.rdb.Transaction;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideGenerator extends AbstractGenerator {
  private boolean entityHasIdentifier;
  private String entityHandleSimpleName;
  private String identifierType;
  private String identifierToEntityChannelSimpleName;
  private String transactionToEntityByIdentifierChannelSimpleName;

  public EntityCrudGuideGenerator(CustomType initiatorType, CustomType entityType) {
    super(initiatorType, entityType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return context.isProcessingFinished(
        Ontology.class, EntityAnnotationFunctions.getCrudOntologyCanonicalName(annotatedType)
    );
  }

  @Override
  public String artifactName() {
    return EntityAnnotationFunctions.getCrudGuideCanonicalName(annotatedType);
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
    vars.put("identifierToEntityChannelSimpleName", identifierToEntityChannelSimpleName);
    vars.put("transactionToEntityByIdentifierChannelSimpleName", transactionToEntityByIdentifierChannelSimpleName);

    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    context.addImport(Guide.class);
    context.addImport(Mapper.class);
    context.addImport(Transaction.class);

    entityHandleSimpleName = context.addToImportAndGetSimpleName(
        EntityAnnotationFunctions.getEntityHandleCanonicalName(annotatedType)
    );
    analyzeEntityIdentifier();
    return true;
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityAnnotationFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierType = context.addToImportAndGetSimpleName(
        EntityAnnotationFunctions.getIdentifierType(annotatedType, identifierMethod.orElseThrow())
    );
    identifierToEntityChannelSimpleName = EntityAnnotationFunctions.getIdentifierToEntityChannelSimpleName(
        annotatedType);
    transactionToEntityByIdentifierChannelSimpleName = EntityAnnotationFunctions.getTransactionToEntityByIdentifierChannelSimpleName(
        annotatedType
    );
  }
}
