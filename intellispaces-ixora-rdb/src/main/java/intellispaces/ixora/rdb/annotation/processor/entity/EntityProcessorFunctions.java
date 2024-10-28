package intellispaces.ixora.rdb.annotation.processor.entity;

import intellispaces.common.base.collection.Collectors;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.jaquarius.object.ObjectFunctions;
import jakarta.persistence.Id;

import java.util.Optional;

public interface EntityProcessorFunctions {

  static String getEntityHandleCanonicalName(CustomType entityType) {
    return ObjectFunctions.getCommonObjectHandleTypename(entityType);
  }

  static String getCrudOntologyCanonicalName(CustomType entityType) {
    return TextFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudOntology");
  }

  static String getCrudOntologySimpleName(CustomType entityType) {
    return TypeFunctions.getSimpleName(getCrudOntologyCanonicalName(entityType));
  }

  static String getCrudGuideCanonicalName(CustomType entityType) {
    return TextFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide");
  }

  static String getCrudGuideGeneratedImplCanonicalName(CustomType entityType) {
    return TypeFunctions.addPrefixToSimpleName(
        "Default", TextFunctions.replaceTailOrElseThrow(entityType.canonicalName(), "Domain", "CrudGuide")
    );
  }

  static Optional<MethodStatement> findIdentifierMethod(CustomType entityType) {
    return entityType.declaredMethods().stream()
        .filter(m -> m.hasAnnotation(Id.class))
        .collect(Collectors.optional());
  }

  static String getIdentifierType(CustomType entityType, MethodStatement identifierMethod) {
    TypeReference returnType = identifierMethod.returnType()
        .orElseThrow(() -> UnexpectedViolationException.withMessage(
            "Entity identifier method {0} of the entity {1} should return value",
            identifierMethod.name(), entityType.canonicalName()
        ));
    return ObjectFunctions.getCommonObjectHandleTypename(returnType);
  }

  static String getIdentifierToEntityChannelSimpleName(CustomType entityType) {
    return "IdentifierTo" +
            TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "Channel";
  }

  static String getTransactionToEntityByIdentifierChannelSimpleName(CustomType entityType) {
    return "TransactionTo" +
            TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "ByIdentifierChannel";
  }

  static String getIdentifierToEntityGuideSimpleName(CustomType entityType) {
    return "IdentifierTo" +
            TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "Guide";
  }

  static String getTransactionToEntityByIdentifierGuideSimpleName(CustomType entityType) {
    return "TransactionTo" +
            TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(entityType.simpleName(), "Domain")) +
        "ByIdentifierGuide";
  }
}
