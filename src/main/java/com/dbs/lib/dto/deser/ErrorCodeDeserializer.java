/**
 * ErrorCodeDeserializer
 */
package com.dbs.lib.dto.deser;

import java.io.IOException;

import org.apache.commons.lang3.math.NumberUtils;

import com.dbs.lib.dto.enumeration.ErrorCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * deserialize by {@link ErrorCode} code
 * 
 * @author dbs on Feb 19, 2018 6:53:19 PM
 * @since 0.1.10
 * @version 1.0
 *
 */
public class ErrorCodeDeserializer extends JsonDeserializer<ErrorCode> {

  @Override
  public ErrorCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonToken t = jp.getCurrentToken();
    if (t == JsonToken.VALUE_NULL) {
      return ErrorCode.none;
    }
    if (t == JsonToken.VALUE_NUMBER_INT) {
      return ErrorCode.find(jp.getValueAsInt(-1));
    }
    if (t == JsonToken.VALUE_STRING) {
      return ErrorCode.find(NumberUtils.toInt(jp.getText()));
    }
    return ErrorCode.none;
  }
}
