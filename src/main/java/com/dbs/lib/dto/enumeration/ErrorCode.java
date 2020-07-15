/**
 * ErrorCode
 */
package com.dbs.lib.dto.enumeration;

import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author dbs 
 * @since 1.0.0
 * @version 1.0
 *
 *     does not overlap with {@link org.springframework.http.HttpStatus}<br>
 */
public enum ErrorCode  implements Iterable<String>, Comparator<ErrorCode> {
  
  none(-1, "none", "error code not set", HttpStatus.NOT_IMPLEMENTED),
  /**
   * 0 Success
   */
  success(0, "Success", "Success", HttpStatus.OK),
  /**
   * 10 generic error, a detailed erro must be provided
   */
  error(10, "generic error", "a detailed error must be provided", HttpStatus.INTERNAL_SERVER_ERROR),
  /**
   * 30 Format error, message format received not conform to specification
   */
  format(30, "Format error", "Format error, message format received not conform to specification", HttpStatus.BAD_REQUEST),
  /**
   * 401 authentication
   */
  authentication(401, "Unauthorized", "User not authenticated", HttpStatus.UNAUTHORIZED), 
  /**
   * 404 not found error, item not found
   */
  notFound(404, "item not found", "", HttpStatus.NOT_FOUND),
  /**
   * 409 not found see {@link org.springframework.http.HttpStatus}
   */
  conflict(409, "Conflict", "The request could not be completed due to a conflict with the current state of the target resource", HttpStatus.CONFLICT),
  /**
   * 50 duplicate
   */
  dup(50, "duplicate", "", HttpStatus.EXPECTATION_FAILED),
  
  /**
   * 10000
   */
  internalError(10000, "internal error", "Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR), 
  
  ;

  final int code;
  final String label;
  /**
   * description or message resource to fetch
   */
  final String description;
  final HttpStatus httpStatus;
  
  ErrorCode(int code, String label, String description, HttpStatus httpStatus) {
    this.code = code;
    this.label = label;
    this.description = description;
    this.httpStatus = httpStatus;
  }

  /**
   * @return code as an integer
   */
  @JsonValue
  public int getCode() {
    return code;
  }

  /**
   * 
   * @return enum label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return the description or message resource id to fetch
   */
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return String.format("%d,%s", code, description);
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      private int index = 0;

      @Override
      public boolean hasNext() {
        int size = ErrorCode.values().length;
        if (index >= size) {
          index = 0;// reinit
          return false;// end iteration
        }
        return index < size;
      }

      @Override
      public String next() {
        switch (index) {
          default:
            return ErrorCode.values()[index++].getLabel();
        }
      }

      @Override // it makes no sense on an enum
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * search by integer code
   * 
   * @param code to find
   * @return {@link ErrorCode}
   */
  public static ErrorCode find(int code) {
    for (ErrorCode e : ErrorCode.values()) {
      if (e.getCode() == code) {
        return e;
      }
    }
    return none;
  }

  @Override
  public int compare(ErrorCode o1, ErrorCode o2) {
    return NumberUtils.compare(o1.code, o2.code);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
