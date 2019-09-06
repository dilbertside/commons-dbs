/**
 * ServiceRuntimeException
 */
package com.dbs.lib.exception;

import com.dbs.lib.dto.enumeration.ErrorCode;

/**
 * @author dbs at 13 Jun 2019 09:05:17
 * @since 1.0.0
 * @version 1.0
 */
@lombok.Getter
@lombok.Setter
public class ServiceRuntimeException extends RuntimeException {

  ErrorCode errorCode;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * @param errorCode
   */
  public ServiceRuntimeException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
