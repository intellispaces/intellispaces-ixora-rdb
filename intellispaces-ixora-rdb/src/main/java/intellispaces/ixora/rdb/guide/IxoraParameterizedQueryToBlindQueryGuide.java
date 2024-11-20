package intellispaces.ixora.rdb.guide;

import intellispaces.ixora.data.collection.Lists;
import intellispaces.ixora.rdb.BlindQueryAndParameterNames;
import intellispaces.ixora.rdb.BlindQueryAndParameterNamesData;
import intellispaces.ixora.rdb.ParameterizedQueryToBlindQueryGuide;
import intellispaces.jaquarius.annotation.Guide;

import java.util.ArrayList;
import java.util.List;

@Guide
public class IxoraParameterizedQueryToBlindQueryGuide implements ParameterizedQueryToBlindQueryGuide {

  @Override
  public BlindQueryAndParameterNames parameterizedQueryToBlindQuery(String query) {
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
    return new BlindQueryAndParameterNamesData(
      new String(blindQuery, 0, index2),
      Lists.of(paramNames, String.class)
    );
  }
}
