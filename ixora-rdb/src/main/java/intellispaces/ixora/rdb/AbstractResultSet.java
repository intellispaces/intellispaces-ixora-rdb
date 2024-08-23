package intellispaces.ixora.rdb;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.Mapper;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.Mover;
import intellispaces.core.annotation.Name;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.object.DataFunctions;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.ixora.structures.collection.JavaList;
import intellispaces.ixora.structures.collection.List;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@MovableObjectHandle("BasicResultSet")
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
    Constructor<D> constructor = getDataHandleConstructor(dataClass);
    Object[] arguments = makeDataHandleArguments(dataClass, constructor);
    try {
      return constructor.newInstance(arguments);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
    }
  }

  @Mover
  @Override
  public <D> List<D> values(Class<D> dataClass) {
    Constructor<D> constructor = getDataHandleConstructor(dataClass);
    java.util.List<D> values = new ArrayList<>();
    while (next()) {
      Object[] arguments = makeDataHandleArguments(dataClass, constructor);
      try {
        values.add(constructor.newInstance(arguments));
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create data handle");
      }
    }
    return new JavaList<>(values, dataClass);
  }

  @SuppressWarnings("unchecked")
  private <D> Constructor<D> getDataHandleConstructor(Class<D> dataClass) {
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(dataClass);
    if (!DataFunctions.isDataDomain(domainClass)) {
      throw UnexpectedViolationException.withMessage("Expected object handle class of the data domain. " +
          "Data domain should be annotated with @{}", Data.class.getSimpleName());
    }
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

  private <D> Object[] makeDataHandleArguments(Class<D> dataClass, Constructor<D> constructor) {
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
      setArgument(arguments, index, alias, paramClass);
    }
    return arguments;
  }

  private void setArgument(Object[] arguments, int index, String alias, Class<?> aClass) {
    if (aClass == int.class) {
      arguments[index] = intValue(alias);
    } else if (aClass == Integer.class) {
      arguments[index] = integerValue(alias);
    } else if (aClass == String.class) {
      arguments[index] = stringValue(alias);
    }
  }
}
