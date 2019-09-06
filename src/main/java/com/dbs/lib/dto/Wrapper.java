/**
 * 
 */
package com.dbs.lib.dto;

/**
 * local variable defined in enclosing scope
 * used in case of stackoverflow.com/questions/43194089 and 25894509
 * @author lch at 6 Sep 2019 17:02:24
 * @since 1.0.0
 * @version 1.0
 */
@lombok.Data
public class Wrapper<T> {

  T object;
  
}
