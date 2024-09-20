package intellispaces.ixora.rdb;

import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.Transition;

@Ontology
public interface RdbOntology {

  @Transition("d3673390-b743-434a-b7c9-f7dc64976387")
  BlindQueryAndParameterNamesDomain parameterizedQueryToBlindQuery(String query);
}
