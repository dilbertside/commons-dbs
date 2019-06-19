/**
 * SimpleResponse
 */
package com.dbs.lib.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.dbs.lib.dto.deser.ErrorCodeDeserializer;
import com.dbs.lib.dto.enumeration.ErrorCode;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * @author dbs
 * @since 0.1.10
 * @version 1.0
 * @version 1.1 add {@link #additionalProperties}
 *
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class SimpleResponse implements Serializable {

  private static final long serialVersionUID = 2495651389927343478L;

  @NotNull
  @JsonProperty("success")
  Boolean success = true;
  
  /**
   * success is 0
   * error ID
   */
  @JsonProperty("eid")
  @JsonDeserialize(using = ErrorCodeDeserializer.class)
  ErrorCode errorId = ErrorCode.success;
  
  /**
   * error/success message
   */
  @NotNull
  @JsonProperty("msg")
  String message;
  
  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * clone SimpleResponse
   * @param response {@link SimpleResponse}
   */
  public SimpleResponse(SimpleResponse response) {
    this.errorId = response.errorId;
    this.message = response.message;
  }
  
  /**
   * {@link #success} is set to false if {@link #errorId} != ErrorCode.success
   * @param errorId error id for contact center
   * @param message subscriber
   */
  public SimpleResponse(ErrorCode errorId, String message) {
    this.success = errorId == ErrorCode.success;
    this.errorId = errorId;
    this.message = message;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @JsonIgnore
  public Object getProperty(String key) {
    return this.additionalProperties.getOrDefault(key, "");
  }
  
  /**
   * {@link #success} is set to false
   * @param errorId {@link ErrorCode}
   * @param message error to set
   * @return SimpleResponse for convenience chaining
   */
  @JsonIgnore
  public SimpleResponse setError(ErrorCode errorId, String message) {
    this.errorId = errorId;
    this.success = errorId == ErrorCode.success ? true : false;
    this.message = message;
    return this;
  }
  
  /**
   * 
   * @param toClone {@link SimpleResponse} to update from
   * @return SimpleResponse for convenience chaining
   */
  @JsonIgnore
  public SimpleResponse update(SimpleResponse toClone) {
    setError(toClone.errorId, toClone.message);
    if (toClone.success) {
      this.success = toClone.success; 
    }
    return this;
  }
}
