package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Ontology;
import intellispaces.jaquarius.processor.AbstractGenerator;
import intellispaces.jaquarius.id.RepetableUuidIdentifierGenerator;
import intellispaces.jaquarius.space.domain.DomainFunctions;
import intellispaces.ixora.rdb.TransactionDomain;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    var identifierGenerator = new RepetableUuidIdentifierGenerator(did);
    identifierToEntityCid = identifierGenerator.next();
    transactionToEntityByIdentifierCid = identifierGenerator.next();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityProcessorFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierToEntityChannelSimpleName = EntityProcessorFunctions.getIdentifierToEntityChannelSimpleName(
        annotatedType
    );
    transactionToEntityByIdentifierChannelSimpleName = EntityProcessorFunctions.getTransactionToEntityByIdentifierChannelSimpleName(
        annotatedType
    );
    identifierType = context.addToImportAndGetSimpleName(
      EntityProcessorFunctions.getIdentifierType(annotatedType, identifierMethod.orElseThrow())
    );
  }
}
