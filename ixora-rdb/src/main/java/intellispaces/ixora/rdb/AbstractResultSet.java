package intellispaces.ixora.rdb;

import intellispaces.commons.collection.ArraysFunctions;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.string.StringFunctions;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.Mover;
import intellispaces.core.annotation.Name;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.object.DataFunctions;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.ixora.structures.collection.JavaList;
import intellispaces.ixora.structures.collection.List;
import jakarta.persistence.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ObjectHandle(value = ResultSetDomain.class, name = "BasicResultSet")
public abstract class AbstractResultSet implements MovableResultSet {
  private final java.sql.ResultSet rs;

  public AbstractResultSet(ResultSet rs) {
    this.rs = rs;
  }

  @Mover
  @Override
  public boolean next() {
    try {
      return rs.next();
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not move cursor");
    }
  }

  @Mapper
  @Override
  public Integer integerValue(String name) {
    try {
      int value = rs.getInt(name);
      if (rs.wasNull()) {
        return null;
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {}", name);
    }
  }

  public int intValue(String name) {
    try {
      int value = rs.getInt(name);
      if (rs.wasNull()) {
        throw UnexpectedViolationException.withMessage("Null value of the primitive integer value by name {}", name);
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {}", name);
    }
  }

  @Mapper
  @Override
  public String stringValue(String name) {
    try {
      String value = rs.getString(name);
      if (rs.wasNull()) {
        return null;
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read string value by name {}", name);
    }
  }

  @Mapper
  @Override
  public <D> D value(Class<D> dataClass) {
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    Object[] arguments = makeDataHandleArguments(dataClass, domainClass, constructor);
    try {
      return constructor.newInstance(arguments);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
    }
  }

  @Mover
  @Override
  public <D> List<D> values(Class<D> dataClass) {
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    java.util.List<D> values = new ArrayList<>();
    while (next()) {
      Object[] arguments = makeDataHandleArguments(dataClass, domainClass, constructor);
      try {
        values.add(constructor.newInstance(arguments));
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
      }
    }
    return new JavaList<>(values, dataClass);
  }

  @SuppressWarnings("unchecked")
  private <D> Constructor<D> getDataHandleConstructor(Class<D> dataClass, Class<?> domainClass) {
    String dataHandleClassName = NameConventionFunctions.getDataClassName(domainClass.getCanonicalName());
    Class<D> dataHandleClass = (Class<D>) TypeFunctions.getClass(dataHandleClassName).orElseThrow(() ->
        UnexpectedViolationException.withMessage("Could not find data handle class by name {} ",
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
          "Data domain should be annotated with @{}", Data.class.getSimpleName());
    }
    return domainClass;
  }

  private <D> Object[] makeDataHandleArguments(
      Class<D> dataClass, Class<?> domainClass, Constructor<D> constructor
  ) {
    Map<String, String> mapping = makeTransitionAliasToColumnNameMapping(domainClass);

    Object[] arguments = new Object[constructor.getParameterCount()];
    for (int index = 0; index < constructor.getParameterCount(); index++) {
      Parameter param = constructor.getParameters()[index];
      Name name = param.getAnnotation(Name.class);
      if (name == null) {
        throw UnexpectedViolationException.withMessage("Parameter {} of the data class {} constructor " +
            "should be marked with annotation {}", index, dataClass, Name.class.getSimpleName());
      }
      String alias = name.value();
      Class<?> paramClass = param.getType();
      setArgument(arguments, index, mapping.get(alias), paramClass);
    }
    return arguments;
  }

  private void setArgument(Object[] arguments, int index, String columnName, Class<?> paramClass) {
    if (paramClass == int.class) {
      arguments[index] = intValue(columnName);
    } else if (paramClass == Integer.class) {
      arguments[index] = integerValue(columnName);
    } else if (paramClass == String.class) {
      arguments[index] = stringValue(columnName);
    }
  }

  private Map<String, String> makeTransitionAliasToColumnNameMapping(Class<?> domainClass) {
    Map<String, String> mapping = new HashMap<>();
    ArraysFunctions.foreach(domainClass.getDeclaredMethods(), m -> {
      Column column = m.getAnnotation(Column.class);
      if (column == null) {
        throw UnexpectedViolationException.withMessage("Method {} of the class {} should be marked with annotation {}",
            m.getName(), domainClass.getCanonicalName(), Column.class.getSimpleName()
        );
      }
      if (StringFunctions.isNullOrBlank(column.name())) {
        throw UnexpectedViolationException.withMessage("Name attribute should be defined in annotation {} " +
                "on the method {} in class {}",
            Column.class.getSimpleName(), m.getName(), domainClass.getCanonicalName()
        );
      }
      mapping.put(m.getName(), column.name());
    });
    return mapping;
  }
}
