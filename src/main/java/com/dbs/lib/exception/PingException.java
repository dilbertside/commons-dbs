/**
 * PingException
 */
package com.dbs.lib.exception;

/**
 * @author dbs on Sep 13, 2016 10:18:57 AM
 * @since 1.0.0
 * @version 1.0
 *
 */
public class PingException extends Exception {

  private static final long serialVersionUID = -2767103698013284779L;

  /**
   * 
   */
  public PingException() {
    super();
  }

  /**
   * @param message to set
   * @param cause to set
   * @param enableSuppression to set
   * @param writableStackTrace to set
   */
  public PingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * @param message to set
   * @param cause to set
   */
  public PingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message to set
   */
  public PingException(String message) {
    super(message);
  }

  /**
   * @param cause to set
   */
  public PingException(Throwable cause) {
    super(cause);
  }

}
