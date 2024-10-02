package intellispaces.ixora.rdb.guide;

import intellispaces.common.base.collection.ArraysFunctions;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.MapperOfMoving;
import intellispaces.framework.core.annotation.Name;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.object.DataFunctions;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.ixora.rdb.ResultSet;
import intellispaces.ixora.rdb.ResultSetToDataListChannel;
import intellispaces.ixora.rdb.ResultSetToDataChannel;
import intellispaces.ixora.structures.collection.List;
import intellispaces.ixora.structures.collection.Lists;
import jakarta.persistence.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Guide
public class IxoraResultSetToDataGuide {

  @Mapper(ResultSetToDataChannel.class)
  public <D> D resultSetToData(ResultSet resultSet, Class<D> dataClass) {
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    Object[] arguments = makeDataHandleArguments(resultSet, dataClass, domainClass, constructor);
    try {
      return constructor.newInstance(arguments);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
    }
  }

  @MapperOfMoving(ResultSetToDataListChannel.class)
  public <D> List<D> resultSetToDataList(ResultSet resultSet, Class<D> dataClass) {
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    java.util.List<D> values = new ArrayList<>();
    while (resultSet.next()) {
      Object[] arguments = makeDataHandleArguments(resultSet, dataClass, domainClass, constructor);
      try {
        values.add(constructor.newInstance(arguments));
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
      }
    }
    return Lists.of(values, dataClass);
  }

  @SuppressWarnings("unchecked")
  private <D> Constructor<D> getDataHandleConstructor(Class<D> dataClass, Class<?> domainClass) {
    String dataHandleClassName = NameConventionFunctions.getDataClassName(domainClass.getCanonicalName());
    Class<D> dataHandleClass = (Class<D>) TypeFunctions.getClass(dataHandleClassName).orElseThrow(() ->
        UnexpectedViolationException.withMessage("Could not find data handle class by name {0} ",
            dataHandleClassName)
    );

    Constructor<?>[] constructors = dataHandleClass.getConstructors();
    if (constructors.length > 1) {
      throw UnexpectedViolationException.withMessage("Data handle class should have one constructor");
    }
    return (Constructor<D>) constructors[0];
  }

  private <D> Class<?> getDomainClass(Class<D> dataClass) {
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(dataClass);
    if (!DataFunctions.isDataDomain(domainClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class of the data domain. " +
          "Data domain should be annotated with @{0}", Data.class.getSimpleName());
    }
    return domainClass;
  }

  private <D> Object[] makeDataHandleArguments(
      ResultSet resultSet, Class<D> dataClass, Class<?> domainClass, Constructor<D> constructor
  ) {
    Map<String, String> mapping = makeChannelAliasToColumnNameMapping(domainClass);

    Object[] arguments = new Object[constructor.getParameterCount()];
    for (int index = 0; index < constructor.getParameterCount(); index++) {
      Parameter param = constructor.getParameters()[index];
      Name name = param.getAnnotation(Name.class);
      if (name == null) {
        throw UnexpectedViolationException.withMessage("Parameter {0} of the data class {1} constructor " +
            "should be marked with annotation {2}", index, dataClass, Name.class.getSimpleName());
      }
      String alias = name.value();
      Class<?> paramClass = param.getType();
      setArgument(resultSet, arguments, index, mapping.get(alias), paramClass);
    }
    return arguments;
  }

  private void setArgument(
      ResultSet resultSet, Object[] arguments, int index, String columnName, Class<?> paramClass
  ) {
    if (paramClass == int.class) {
      Integer value = resultSet.integerValue(columnName);
      if (value == null) {
        throw UnexpectedViolationException.withMessage("Null value of the primitive integer value by name {0}",
            columnName);
      }
      arguments[index] = value;
    } else if (paramClass == Integer.class) {
      arguments[index] = resultSet.integerValue(columnName);
    } else if (paramClass == String.class) {
      arguments[index] = resultSet.stringValue(columnName);
    }
  }

  private Map<String, String> makeChannelAliasToColumnNameMapping(Class<?> domainClass) {
    Map<String, String> mapping = new HashMap<>();
    ArraysFunctions.foreach(domainClass.getDeclaredMethods(), method -> {
      Column column = method.getAnnotation(Column.class);
      if (column == null) {
        throw UnexpectedViolationException.withMessage("Method {0} of the class {1} should be marked with annotation {2}",
            method.getName(), domainClass.getCanonicalName(), Column.class.getSimpleName()
        );
      }
      if (TextFunctions.isNullOrBlank(column.name())) {
        throw UnexpectedViolationException.withMessage("Name attribute should be defined in annotation {0} " +
                "on the method {1} in class {2}",
            Column.class.getSimpleName(), method.getName(), domainClass.getCanonicalName()
        );
      }
      mapping.put(method.getName(), column.name());
    });
    return mapping;
  }
}
