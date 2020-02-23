/**
 * Wrapper
 */
package com.dbs.lib.dto;

/**
 * local variable defined in enclosing scope
 * used in case of stackoverflow.com/questions/43194089 and 25894509
 * @author dbs at 6 Sep 2019 17:02:24
 * @since 1.0.8
 * @version 1.1
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Wrapper<T> {

  T object;
  
}
