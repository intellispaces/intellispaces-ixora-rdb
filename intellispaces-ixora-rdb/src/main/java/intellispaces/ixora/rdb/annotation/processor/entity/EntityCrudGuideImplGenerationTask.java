package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.processor.AbstractGenerationTask;
import intellispaces.ixora.rdb.Transaction;
import intellispaces.ixora.rdb.exception.RdbException;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityCrudGuideImplGenerationTask extends AbstractGenerationTask {
  private String entityHandleSimpleName;
  private String entityTable;
  private String entityAlias;
  private boolean entityHasIdentifier;
  private String identifierType;
  private String identifierColumn;
  private String guideType;

  public EntityCrudGuideImplGenerationTask(CustomType initiatorType, CustomType entityType) {
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
    return EntityProcessorFunctions.getCrudGuideGeneratedImplCanonicalName(annotatedType);
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

    guideType = context.addToImportAndGetSimpleName(EntityProcessorFunctions.getCrudGuideCanonicalName(annotatedType));
    entityHandleSimpleName = context.addToImportAndGetSimpleName(
        EntityProcessorFunctions.getEntityHandleCanonicalName(annotatedType)
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
        RdbException.withMessage("RDB entity class {} must annotation with annotation {}",
        annotatedType.canonicalName(), Table.class.getCanonicalName()
    ));
    if (TextFunctions.isNotBlank(table.schema())) {
      return table.schema() + "." + table.name();
    }
    return table.name();
  }

  private void analyzeEntityIdentifier() {
    Optional<MethodStatement> identifierMethod = EntityProcessorFunctions.findIdentifierMethod(annotatedType);
    if (identifierMethod.isEmpty()) {
      entityHasIdentifier = false;
      return;
    }
    entityHasIdentifier = true;

    identifierType = context.addToImportAndGetSimpleName(
        EntityProcessorFunctions.getIdentifierType(annotatedType, identifierMethod.get())
    );

    Column column = identifierMethod.orElseThrow().selectAnnotation(Column.class).orElseThrow(() ->
        RdbException.withMessage("RDB entity {} identifier method {} must annotation with annotation {}",
            annotatedType.canonicalName(), identifierMethod.get().name(), Column.class.getCanonicalName()
        ));
    identifierColumn = column.name();
  }
}
