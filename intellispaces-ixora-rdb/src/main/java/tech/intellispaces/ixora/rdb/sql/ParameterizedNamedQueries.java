package tech.intellispaces.ixora.rdb.sql;

import tech.intellispaces.jaquarius.ixora.data.collection.UnmovableListHandle;
import tech.intellispaces.jaquarius.ixora.rdb.sql.UnmovableParameterizedNamedQueryHandle;

public interface ParameterizedNamedQueries {

  static UnmovableParameterizedNamedQueryHandle get(
      String query,
      UnmovableListHandle<String> paramNames
  ) {
    return new ParameterizedNamedQueryWrapper(query, paramNames);
  }
}
