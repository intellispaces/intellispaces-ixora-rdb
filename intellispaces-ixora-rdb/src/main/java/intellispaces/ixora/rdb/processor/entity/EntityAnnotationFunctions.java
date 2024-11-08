package intellispaces.ixora.rdb.processor.entity;

import intellispaces.common.base.exception.UnexpectedExceptions;
import intellispaces.common.base.stream.Collectors;
import intellispaces.common.base.text.StringFunctions;
import intellispaces.common.base.type.ClassNameFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.jaquarius.object.ObjectFunctions;
import jakarta.persistence.Id;

import java.util.Optional;

public interface EntityAnnotationFunctions {

  static String getEntityHandleCanonicalName(CustomType entityType) {
    return ObjectFunctions.getCommonObjectHandleTypename(entityType);
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
    return ObjectFunctions.getCommonObjectHandleTypename(returnType);
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
