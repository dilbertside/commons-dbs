package com.dbs.lib.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 * 
 * @author dbs at 19 Jun 2019 12:38:38
 * @since 1.0.0
 * @version 1.0
 */
public class UserNotActivatedException extends AuthenticationException {

  private static final long serialVersionUID = 1L;

  public UserNotActivatedException(String message) {
    super(message);
  }

  public UserNotActivatedException(String message, Throwable t) {
    super(message, t);
  }
}
