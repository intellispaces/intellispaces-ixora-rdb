package tech.intellispaces.ixora.rdb.processor.entity;

import jakarta.persistence.Id;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.stream.Collectors;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassNameFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.Optional;

public interface EntityAnnotationFunctions {

  static String getEntityHandleCanonicalName(CustomType entityType) {
    return ObjectHandleFunctions.getCommonObjectHandleTypename(entityType);
  }

  static String getCrudOntologyCanonicalName(CustomType entityType) {
    return StringFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudOntology");
  }

  static String getCrudOntologySimpleName(CustomType entityType) {
    return ClassNameFunctions.getSimpleName(getCrudOntologyCanonicalName(entityType));
  }

  static String getCrudGuideCanonicalName(CustomType entityType) {
    return StringFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide");
  }

  static String getCrudGuideGeneratedImplCanonicalName(CustomType entityType) {
    return ClassNameFunctions.addPrefixToSimpleName(
        "Default", StringFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide")
    );
  }

  static Optional<MethodStatement> findIdentifierMethod(CustomType entityType) {
    return entityType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Id.class))
        .collect(Collectors.optional());
  }

  static String getIdentifierType(CustomType entityType, MethodStatement identifierMethod) {
    TypeReference returnType = identifierMethod.returnType()
        .orElseThrow(() -> UnexpectedExceptions.withMessage(
            "Entity identifier method {0} of the entity {1} should return value",
            identifierMethod.name(), entityType.canonicalName()
        ));
    return ObjectHandleFunctions.getCommonObjectHandleTypename(returnType);
  }

  static String getIdentifierToEntityChannelSimpleName(CustomType entityType) {
    return "IdentifierTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "Channel";
  }

  static String getTransactionToEntityByIdentifierChannelSimpleName(CustomType entityType) {
    return "TransactionTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "ByIdentifierChannel";
  }

  static String getIdentifierToEntityGuideSimpleName(CustomType entityType) {
    return "IdentifierTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "Guide";
  }

  static String getTransactionToEntityByIdentifierGuideSimpleName(CustomType entityType) {
    return "TransactionTo" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "ByIdentifierGuide";
  }
}