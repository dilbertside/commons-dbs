/**
 * ExcelColumn
 */
package com.dbs.lib.spreadsheet;

/**
 * https://codereview.stackexchange.com/questions/44545/excel-column-string-to-row-number-and-vice-versa
 * 
 * @author lch at 4 Jan 2020 14:41:28
 * @since 1.0.0
 * @version 1.0
 */
@lombok.experimental.UtilityClass
public class ExcelColumn {

  public static int toNumber(String name) {
    int number = 0;
    for (int i = 0; i < name.length(); i++) {
      number = number * 26 + (name.charAt(i) - ('A' - 1));
    }
    return number;
  }

  public static String toName(int number) {
    StringBuilder sb = new StringBuilder();
    while (number-- > 0) {
      sb.append((char) ('A' + (number % 26)));
      number /= 26;
    }
    return sb.reverse().toString();
  }
}
