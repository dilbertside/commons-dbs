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
 * @version 1.1 add {@link #ServiceException(ErrorCode, String, Throwable)}
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
	 * Constructs an {@code IOException} with the specified detail message and error code
	 * 
	 * @param errorCode {@link ErrorCode}
	 * @param message The detail message (which is saved for later retrieval
	 * by the {@link #getMessage()} method)
	 */
	public ServiceException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 
	 * Constructs an {@code IOException} with the specified detail message
	 * and cause.
	 *
	 * <p>
	 * Note that the detail message associated with {@code cause} is
	 * <i>not</i> automatically incorporated into this exception's detail
	 * message.
	 *
	 * @param errorCode {@link ErrorCode}
	 * @param message The detail message (which is saved for later retrieval
	 * by the {@link #getMessage()} method)
	 *
	 * @param cause The cause (which is saved for later retrieval by the
	 * {@link #getCause()} method). (A null value is permitted,
	 * and indicates that the cause is nonexistent or unknown.)
	 */
	public ServiceException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

}
