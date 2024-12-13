package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.ixora.rdb.TransactionDomain;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.id.RepetableUuidIdentifierGenerator;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;

import java.util.Optional;
import java.util.UUID;

public class EntityCrudOntologyGenerator extends JaquariusArtifactGenerator {
  private String identifierToEntityCid;
  private String transactionToEntityByIdentifierCid;

  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierToEntityChannelSimpleName;
  private String transactionToEntityByIdentifierChannelSimpleName;

  public EntityCrudOntologyGenerator(CustomType entityType) {
    super(entityType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return EntityAnnotationFunctions.getCrudOntologyCanonicalName(sourceArtifact());
  }

  @Override
  protected String templateName() {
    return "/entity_crud_ontology.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Ontology.class);
    addImport(Channel.class);
    addImport(TransactionDomain.class);

    defineIdentifiers();
    analyzeEntityIdentifier();

    addVariable("generatedAnnotation", makeGeneratedAnnotation());
    addVariable("entityHasIdentifier", entityHasIdentifier);
    addVariable("identifierType", identifierType);
    addVariable("identifierToEntityChannelSimpleName", identifierToEntityChannelSimpleName);
    addVariable("transactionToEntityByIdentifierChannelSimpleName", transactionToEntityByIdentifierChannelSimpleName);
    addVariable("identifierToEntityCid", identifierToEntityCid);
    addVariable("transactionToEntityByIdentifierCid", transactionToEntityByIdentifierCid);
    return true;
  }

  private void defineIdentifiers() {
    String did = DomainFunctions.getDomainId(sourceArtifact());
    var identifierGenerator = new RepetableUuidIdentifierGenerator(UUID.fromString(did));
    identifierToEntityCid = identifierGenerator.next();
    transactionToEntityByIdentifierCid = identifierGenerator.next();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityAnnotationFunctions.findIdentifierMethod(sourceArtifact());
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierToEntityChannelSimpleName = EntityAnnotationFunctions.getIdentifierToEntityChannelSimpleName(
        sourceArtifact()
    );
    transactionToEntityByIdentifierChannelSimpleName = EntityAnnotationFunctions.getTransactionToEntityByIdentifierChannelSimpleName(
        sourceArtifact()
    );
    identifierType = addToImportAndGetSimpleName(
      EntityAnnotationFunctions.getIdentifierType(sourceArtifact(), identifierMethod.orElseThrow())
    );
  }
}
