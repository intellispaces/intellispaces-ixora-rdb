package tech.intellispaces.ixora.rdb.sql;

import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.ixora.data.collection.UnmovableListHandle;
import tech.intellispaces.jaquarius.ixora.rdb.sql.ParameterizedNamedQueryDomain;
import tech.intellispaces.jaquarius.ixora.rdb.sql.UnmovableParameterizedNamedQueryHandle;

@ObjectHandle(ParameterizedNamedQueryDomain.class)
public abstract class ParameterizedNamedQuery implements UnmovableParameterizedNamedQueryHandle {
  private final String query;
  private final UnmovableListHandle<String> paramNames;

  public ParameterizedNamedQuery(
      String query,
      UnmovableListHandle<String> paramNames
  ) {
    this.query = query;
    this.paramNames = paramNames;
  }

  @Mapper
  @Override
  public String query() {
    return query;
  }

  @Mapper
  @Override
  public UnmovableListHandle<String> paramNames() {
    return paramNames;
  }
}
