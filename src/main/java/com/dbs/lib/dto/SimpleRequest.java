/**
 * SimpleRequest
 */
package com.dbs.lib.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author dbs
 * @since 0.0.18
 * @version 1.0
 * @version 1.1 change property name from value to {@link #data}
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class SimpleRequest<T> {

  /**
   * payload request
   */
  @NotNull
  T data;

  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
