/**
 * ServiceRuntimeException
 */
package com.dbs.lib.exception;

import com.dbs.lib.dto.enumeration.ErrorCode;

/**
 * @author dbs at 13 Jun 2019 09:05:17
 * @since 1.0.0
 * @version 1.0
 * @version 1.1 add constructor {@link #ServiceRuntimeException(ServiceException)}
 */
@lombok.Getter
@lombok.Setter
public class ServiceRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 7263090681973996878L;
  
  ErrorCode errorCode;
  
  /**
   * @param errorCode
   */
  public ServiceRuntimeException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
  
  public ServiceRuntimeException(ServiceException e) {
    this(e.errorCode, e.getMessage());
    addSuppressed(e);
  }

}
