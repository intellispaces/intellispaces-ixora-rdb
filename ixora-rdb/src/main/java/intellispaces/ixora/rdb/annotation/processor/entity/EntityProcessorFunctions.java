package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.commons.collection.AdditionalCollectors;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.string.StringFunctions;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.javastatements.Statement;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.TypeReference;
import jakarta.persistence.Id;

import java.util.Optional;

public interface EntityProcessorFunctions {

  static String getEntityHandleCanonicalName(CustomType entityType) {
    return ObjectFunctions.getBaseObjectHandleTypename(entityType);
  }

  static String getCrudOntologyCanonicalName(CustomType entityType) {
    return StringFunctions.replaceEndingOrElseThrow(entityType.canonicalName(), "Domain", "CrudOntology");
  }

  static String getCrudOntologySimpleName(CustomType entityType) {
    return TypeFunctions.getSimpleName(getCrudOntologyCanonicalName(entityType));
  }

  static String getCrudGuideCanonicalName(CustomType entityType) {
    return StringFunctions.replaceEndingOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide");
  }

  static String getCrudGuideGeneratedImplCanonicalName(CustomType entityType) {
    return TypeFunctions.addPrefixToSimpleName(
        "Generated",
        StringFunctions.replaceEndingOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide")
    );
  }

  static Optional<MethodStatement> findIdentifierMethod(CustomType entityType) {
    return entityType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Id.class))
        .collect(AdditionalCollectors.optional());
  }

  static String getIdentifierType(CustomType entityType, MethodStatement identifierMethod) {
    TypeReference returnType = identifierMethod.returnType()
        .orElseThrow(() -> UnexpectedViolationException.withMessage(
            "Entity identifier method {} of the entity {} should return value",
            identifierMethod.name(), entityType.canonicalName()
        ));
    return ObjectFunctions.getBaseObjectHandleTypename(returnType);
  }

  static String getIdentifierToEntityTransitionSimpleName(CustomType entityType) {
    return "IdentifierTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.replaceEndingOrElseThrow(entityType.simpleName(), "Domain", "")) +
        "Transition";
  }

  static String getTransactionToEntityByIdentifierTransitionSimpleName(CustomType entityType) {
    return "TransactionTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.replaceEndingOrElseThrow(entityType.simpleName(), "Domain", "")) +
        "ByIdentifierTransition";
  }

  static String getIdentifierToEntityGuideSimpleName(CustomType entityType) {
    return "IdentifierTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.replaceEndingOrElseThrow(entityType.simpleName(), "Domain", "")) +
        "Guide";
  }

  static String getTransactionToEntityByIdentifierGuideSimpleName(CustomType entityType) {
    return "TransactionTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.replaceEndingOrElseThrow(entityType.simpleName(), "Domain", "")) +
        "ByIdentifierGuide";
  }
}
