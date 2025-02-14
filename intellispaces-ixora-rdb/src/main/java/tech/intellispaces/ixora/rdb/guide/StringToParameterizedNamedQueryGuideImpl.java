package tech.intellispaces.ixora.rdb.guide;

import tech.intellispaces.ixora.rdb.sql.ParameterizedNamedQueries;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.ixora.data.collection.Lists;
import tech.intellispaces.jaquarius.ixora.rdb.sql.ParameterizedNamedQueryHandle;
import tech.intellispaces.jaquarius.ixora.rdb.StringToParameterizedNamedQueryGuide;

import java.util.ArrayList;
import java.util.List;

@Guide
public class StringToParameterizedNamedQueryGuideImpl implements StringToParameterizedNamedQueryGuide {

  @Override
  public ParameterizedNamedQueryHandle stringToParameterizedNamedQuery(String query) {
    char[] originQuery = query.toCharArray();
    char[] blindQuery = new char[query.length()];
    List<String> paramNames = new ArrayList<>();
    int index1 = 0, index2 = 0;
    while (index1 < originQuery.length) {
      char ch = originQuery[index1++];
      if (ch == ':') {
        blindQuery[index2++] = '?';
        int ind = index1;
        while (index1 < originQuery.length && Character.isLetterOrDigit(originQuery[index1])) {
          index1++;
        }
        String paramName = query.substring(ind, index1);
        paramNames.add(paramName);
      } else {
        blindQuery[index2++] = ch;
      }
    }
    return ParameterizedNamedQueries.get(
        new String(blindQuery, 0, index2),
        Lists.of(paramNames, String.class)
    );
  }
}
