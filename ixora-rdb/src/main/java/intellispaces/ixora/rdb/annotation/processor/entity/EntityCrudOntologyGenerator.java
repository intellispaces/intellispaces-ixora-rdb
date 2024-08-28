package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.core.annotation.Ontology;
import intellispaces.core.annotation.Transition;
import intellispaces.core.annotation.processor.AbstractGenerator;
import intellispaces.core.id.RepetableUuidIdentifierGenerator;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.ixora.rdb.TransactionDomain;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudOntologyGenerator extends AbstractGenerator {
  private String identifierToEntityTid;
  private String transactionToEntityByIdentifierTid;

  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierToEntityTransitionSimpleName;
  private String transactionToEntityByIdentifierTransitionSimpleName;

  public EntityCrudOntologyGenerator(CustomType entityType) {
    super(entityType);
  }

  @Override
  public String getArtifactName() {
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
    context.generatedClassCanonicalName(getArtifactName());

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
