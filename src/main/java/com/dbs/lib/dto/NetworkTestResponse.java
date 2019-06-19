/**
 * NetworkTestResponse
 */
package com.dbs.lib.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dbs on Dec 26, 2016 2:56:42 PM
 * @since 1.0.0
 * @version 1.0
 *
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class NetworkTestResponse implements Serializable {

  private static final long serialVersionUID = 8274255689169410284L;

  /**
   * Digest checksum
   */
  @JsonProperty(value = "msg")
  @Size(min = 0, max = 1024)
  String message;

  /**
   * Time of Transaction Request, UTC (GMT+0000)
   */
  @JsonProperty
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  @NotNull
  ZonedDateTime dateTime = ZonedDateTime.now();

  /**
   * 
   * @param message to set
   */
  public NetworkTestResponse(String message) {
    this();
    this.message = message;
  }
  
  public NetworkTestResponse(NetworkTestRequest dto) {
    this(dto.message);
  }

}
