/**
 * ServiceException
 */
package com.dbs.lib.exception;

import java.io.IOException;

import com.dbs.lib.dto.enumeration.ErrorCode;

/**
 * @author dbs at 13 Jun 2019 09:05:17
 * @since 1.0.0
 * @version 1.0
 */
@lombok.Getter
@lombok.Setter
public class ServiceException extends IOException {

  ErrorCode errorCode;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * @param errorCode
   */
  public ServiceException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
