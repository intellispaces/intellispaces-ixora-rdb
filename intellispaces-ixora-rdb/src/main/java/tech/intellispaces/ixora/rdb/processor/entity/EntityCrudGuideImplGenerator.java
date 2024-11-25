package tech.intellispaces.ixora.rdb.processor.entity;

import tech.intellispaces.ixora.rdb.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import tech.intellispaces.entity.text.StringFunctions;
import tech.intellispaces.ixora.data.association.Maps;
import tech.intellispaces.ixora.rdb.Transactions;
import tech.intellispaces.ixora.rdb.annotation.Transactional;
import tech.intellispaces.ixora.rdb.exception.RdbExceptions;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideImplGenerator extends AbstractGenerator {
  private String entityHandleSimpleName;
  private String entityTable;
  private String entityAlias;
  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierColumn;
  private String guideType;

  public EntityCrudGuideImplGenerator(CustomType initiatorType, CustomType entityType) {
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
    return EntityAnnotationFunctions.getCrudGuideGeneratedImplCanonicalName(annotatedType);
  }

  @Override
  protected String templateName() {
    return "/entity_crud_guide_impl.template";
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
    vars.put("guideType", guideType);
    vars.put("entityHandleSimpleName", entityHandleSimpleName);
    vars.put("entityTable", entityTable);
    vars.put("entityAlias", entityAlias);
    vars.put("entityHasIdentifier", entityHasIdentifier);
    vars.put("identifierType", identifierType);
    vars.put("identifierColumn", identifierColumn);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());

    context.addImport(Guide.class);
    context.addImport(Transaction.class);
    context.addImport(tech.intellispaces.ixora.data.association.Map.class);
    context.addImport(Maps.class);
    context.addImport(Transactional.class);
    context.addImport(Transactions.class);

    guideType = context.addToImportAndGetSimpleName(EntityAnnotationFunctions.getCrudGuideCanonicalName(annotatedType));
    entityHandleSimpleName = context.addToImportAndGetSimpleName(
        EntityAnnotationFunctions.getEntityHandleCanonicalName(annotatedType)
    );

    analyzeEntity();
    return true;
  }

  private void analyzeEntity() {
    entityTable = getTableName();
    entityAlias = entityTable.substring(0, 1).toLowerCase();
    analyzeEntityIdentifier();
  }

  private String getTableName() {
    Table table = annotatedType.selectAnnotation(Table.class).orElseThrow(() ->
        RdbExceptions.withMessage("RDB entity class {0} must annotation with annotation {1}",
        annotatedType.canonicalName(), Table.class.getCanonicalName()
    ));
    if (StringFunctions.isNotBlank(table.schema())) {
      return table.schema() + "." + table.name();
    }
    return table.name();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityAnnotationFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierType = context.addToImportAndGetSimpleName(
        EntityAnnotationFunctions.getIdentifierType(annotatedType, identifierMethod.get())
    );

    Column column = identifierMethod.orElseThrow().selectAnnotation(Column.class).orElseThrow(() ->
        RdbExceptions.withMessage("RDB entity {0} identifier method {1} must annotation with annotation {2}",
            annotatedType.canonicalName(), identifierMethod.get().name(), Column.class.getCanonicalName()
        ));
    identifierColumn = column.name();
  }
}
