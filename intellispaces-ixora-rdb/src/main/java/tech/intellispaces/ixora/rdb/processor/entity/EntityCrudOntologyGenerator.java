package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.ixora.rdb.TransactionDomain;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import tech.intellispaces.jaquarius.id.RepetableUuidIdentifierGenerator;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EntityCrudOntologyGenerator extends AbstractGenerator {
  private String identifierToEntityCid;
  private String transactionToEntityByIdentifierCid;

  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierToEntityChannelSimpleName;
  private String transactionToEntityByIdentifierChannelSimpleName;

  public EntityCrudOntologyGenerator(CustomType initiatorType, CustomType entityType) {
    super(initiatorType, entityType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return EntityAnnotationFunctions.getCrudOntologyCanonicalName(annotatedType);
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
    vars.put("identifierToEntityChannelSimpleName", identifierToEntityChannelSimpleName);
    vars.put("transactionToEntityByIdentifierChannelSimpleName", transactionToEntityByIdentifierChannelSimpleName);

    vars.put("identifierToEntityCid", identifierToEntityCid);
    vars.put("transactionToEntityByIdentifierCid", transactionToEntityByIdentifierCid);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());

    context.addImport(Ontology.class);
    context.addImport(Channel.class);
    context.addImport(TransactionDomain.class);

    defineIdentifiers();
    analyzeEntityIdentifier();
    return true;
  }

  private void defineIdentifiers() {
    String did = DomainFunctions.getDomainId(annotatedType);
    var identifierGenerator = new RepetableUuidIdentifierGenerator(UUID.fromString(did));
    identifierToEntityCid = identifierGenerator.next();
    transactionToEntityByIdentifierCid = identifierGenerator.next();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityAnnotationFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierToEntityChannelSimpleName = EntityAnnotationFunctions.getIdentifierToEntityChannelSimpleName(
        annotatedType
    );
    transactionToEntityByIdentifierChannelSimpleName = EntityAnnotationFunctions.getTransactionToEntityByIdentifierChannelSimpleName(
        annotatedType
    );
    identifierType = context.addToImportAndGetSimpleName(
      EntityAnnotationFunctions.getIdentifierType(annotatedType, identifierMethod.orElseThrow())
    );
  }
}
