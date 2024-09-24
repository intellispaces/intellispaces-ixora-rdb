package intellispaces.ixora.rdb;

import intellispaces.common.base.collection.ArraysFunctions;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.MapperOfMoving;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.Name;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.object.DataFunctions;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.ixora.structures.collection.List;
import intellispaces.ixora.structures.collection.Lists;
import jakarta.persistence.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ObjectHandle(value = ResultSetDomain.class, name = "ResultSetHandleImpl")
public abstract class ResultSetHandle implements MovableResultSet {
  private final java.sql.ResultSet rs;

  public ResultSetHandle(ResultSet rs) {
    this.rs = rs;
  }

  @Override
  @MapperOfMoving
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
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {0}", name);
    }
  }

  public int intValue(String name) {
    try {
      int value = rs.getInt(name);
      if (rs.wasNull()) {
        throw UnexpectedViolationException.withMessage("Null value of the primitive integer value by name {0}", name);
      }
      return value;
    } catch (SQLException e) {
      throw TraverseException.withCauseAndMessage(e, "Could not read integer value by name {0}", name);
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
      throw TraverseException.withCauseAndMessage(e, "Could not read string value by name {0}", name);
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

  @Override
  @MapperOfMoving
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
      Class<D> dataClass, Class<?> domainClass, Constructor<D> constructor
  ) {
    Map<String, String> mapping = makeTransitionAliasToColumnNameMapping(domainClass);

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
        throw UnexpectedViolationException.withMessage("Method {0} of the class {1} should be marked with annotation {2}",
            m.getName(), domainClass.getCanonicalName(), Column.class.getSimpleName()
        );
      }
      if (TextFunctions.isNullOrBlank(column.name())) {
        throw UnexpectedViolationException.withMessage("Name attribute should be defined in annotation {0} " +
                "on the method {1} in class {2}",
            Column.class.getSimpleName(), m.getName(), domainClass.getCanonicalName()
        );
      }
      mapping.put(m.getName(), column.name());
    });
    return mapping;
  }
}
