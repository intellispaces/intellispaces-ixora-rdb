package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.ixora.rdb.TransactionHandle;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;

import java.util.Optional;

public class EntityCrudGuideGenerator extends JaquariusArtifactGenerator {
  private boolean entityHasIdentifier;
  private String entityHandleSimpleName;
  private String identifierType;
  private String identifierToEntityChannelSimpleName;
  private String transactionToEntityByIdentifierChannelSimpleName;

  public EntityCrudGuideGenerator(CustomType entityType) {
    super(entityType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return context.isProcessingFinished(
        EntityAnnotationFunctions.getCrudOntologyCanonicalName(sourceArtifact()), Ontology.class
    );
  }

  @Override
  public String generatedArtifactName() {
    return EntityAnnotationFunctions.getCrudGuideCanonicalName(sourceArtifact());
  }

  @Override
  protected String templateName() {
    return "/entity_crud_guide.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Guide.class);
    addImport(Mapper.class);
    addImport(TransactionHandle.class);

    entityHandleSimpleName = addImportAndGetSimpleName(
        EntityAnnotationFunctions.getEntityHandleCanonicalName(sourceArtifact())
    );

    analyzeEntityIdentifier();

    addVariable("entityHasIdentifier", entityHasIdentifier);
    addVariable("entityHandleSimpleName", entityHandleSimpleName);
    addVariable("identifierType", identifierType);
    addVariable("identifierToEntityChannelSimpleName", identifierToEntityChannelSimpleName);
    addVariable("transactionToEntityByIdentifierChannelSimpleName", transactionToEntityByIdentifierChannelSimpleName);
    return true;
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityAnnotationFunctions.findIdentifierMethod(sourceArtifact());
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierType = addImportAndGetSimpleName(
        EntityAnnotationFunctions.getIdentifierType(sourceArtifact(), identifierMethod.orElseThrow())
    );
    identifierToEntityChannelSimpleName = EntityAnnotationFunctions.getIdentifierToEntityChannelSimpleName(
        sourceArtifact());
    transactionToEntityByIdentifierChannelSimpleName = EntityAnnotationFunctions.getTransactionToEntityByIdentifierChannelSimpleName(
        sourceArtifact()
    );
  }
}
