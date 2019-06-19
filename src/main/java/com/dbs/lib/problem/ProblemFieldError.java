package com.dbs.lib.problem;

import java.io.Serializable;

/**
 * Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 * 
 * @author dbs on Nov 21, 2017 1:47:26 PM
 * @since 1.0.0
 * @version 1.0
 * @version 1.1 add {@link #code}
 *
 */
public class ProblemFieldError implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * DTO instance name
   */
  private final String objectName;

  /**
   * DTO field name
   */
  private final String field;

  /**
   * default message, usually English, to get a translation {@link ProblemFieldError#code}
   */
  private final String message;
  /**
   * code to retrieve a translation 
   */
  private final String code;

  /**
   * 
   * @param dto to set
   * @param field to set
   * @param message to set
   * @param codes to set
   */
  public ProblemFieldError(String dto, String field, String message, String[] codes) {
    this.objectName = dto;
    this.field = field;
    this.message = message;
    this.code = (codes != null && codes.length > 0 ? codes[codes.length - 1] : null);
  }

  public String getObjectName() {
    return objectName;
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

}
