/**
 * NetworkTestRequest
 */
package com.dbs.lib.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author dbs
 * @since 1.0.0
 * @version 1.0
 *
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkTestRequest implements Serializable {

  private static final long serialVersionUID = 1607381361915999018L;

  /**
   * Digest checksum
   */
  @JsonProperty(value = "msg")
  @Size(min = 0, max = 1024)
  String message;

}
