package intellispaces.ixora.rdb;

import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.traverse.TraverseTypes;

@Domain("80898b0a-6a68-4693-bc86-17d7c5bb6a64")
public interface ResultSet {

  @Transition(value = "a0b049ef-9ff2-4611-95b4-b500673afa55", allowedTraverseTypes = TraverseTypes.Moving)
  boolean next();

  @Transition(value = "77aeebe4-6c9f-4067-a1be-fe7633558a13", name = "ResultSetToIntegerValueByNameTransition")
  Integer integerValue(String name);

  @Transition(value = "0fb39956-45e1-4b78-afdf-aa0b4922ed86", name = "ResultSetToStringValueByNameTransition")
  String stringValue(String name);

  @Transition("d280d0b1-16d9-4d3a-a172-a0ad39c63de7")
  <T> T rowValue(Class<T> valueDomain);
}
