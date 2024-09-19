package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.annotation.processor.AbstractGenerationTask;
import intellispaces.framework.core.id.RepetableUuidIdentifierGenerator;
import intellispaces.framework.core.space.domain.DomainFunctions;
import intellispaces.ixora.rdb.TransactionDomain;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudOntologyGenerationTask extends AbstractGenerationTask {
  private String identifierToEntityTid;
  private String transactionToEntityByIdentifierTid;

  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierToEntityTransitionSimpleName;
  private String transactionToEntityByIdentifierTransitionSimpleName;

  public EntityCrudOntologyGenerationTask(CustomType initiatorType, CustomType entityType) {
    super(initiatorType, entityType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return EntityProcessorFunctions.getCrudOntologyCanonicalName(annotatedType);
  }

  @Override
  protected String templateName() {
    return "/entity_crud_ontology.template";
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
    vars.put("identifierType", identifierType);
    vars.put("identifierToEntityTransitionSimpleName", identifierToEntityTransitionSimpleName);
    vars.put("transactionToEntityByIdentifierTransitionSimpleName", transactionToEntityByIdentifierTransitionSimpleName);

    vars.put("identifierToEntityTid", identifierToEntityTid);
    vars.put("transactionToEntityByIdentifierTid", transactionToEntityByIdentifierTid);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());

    context.addImport(Ontology.class);
    context.addImport(Transition.class);
    context.addImport(TransactionDomain.class);

    defineIdentifiers();
    analyzeEntityIdentifier();
    return true;
  }

  private void defineIdentifiers() {
    String did = DomainFunctions.getDomainId(annotatedType);
    var identifierGenerator = new RepetableUuidIdentifierGenerator(did);
    identifierToEntityTid = identifierGenerator.next();
    transactionToEntityByIdentifierTid = identifierGenerator.next();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityProcessorFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierToEntityTransitionSimpleName = EntityProcessorFunctions.getIdentifierToEntityTransitionSimpleName(
        annotatedType
    );
    transactionToEntityByIdentifierTransitionSimpleName = EntityProcessorFunctions.getTransactionToEntityByIdentifierTransitionSimpleName(
        annotatedType
    );
    identifierType = context.addToImportAndGetSimpleName(
      EntityProcessorFunctions.getIdentifierType(annotatedType, identifierMethod.orElseThrow())
    );
  }
}
