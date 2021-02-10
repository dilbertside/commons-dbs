/**
 * TestUtilRestDoc
 */
package com.dbs.lib.test;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.zalando.problem.AbstractThrowableProblem;

import com.dbs.lib.dto.NetworkTestRequest;
import com.dbs.lib.dto.NetworkTestResponse;
import com.dbs.lib.dto.SimpleRequest;
import com.dbs.lib.dto.SimpleResponse;
import com.dbs.lib.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Joiner;

/**
 * @author lch on Mar 5, 2018 5:00:32 PM
 * @since 0.3.6
 * @version 1.0
 *
 */
public class TestUtilRestDoc {

  public static FieldDescriptor[] problemFields, simpleRequestFields, simpleRespFields, simpleRespFieldsWithData, defaultFields, networkTestRequestFields, networkTestResponseFields
                                  , userRequestFields, userReqFields, userRespFields, userRespDataFields;
  public static FieldDescriptor dateTime;
  
  static ConstraintDescriptions simpleResponseConstraints;
  
  public static class DefaultProblemEx extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;
    public List<Object> stackTrace;
  }
  
  /**
   * how to use
   * <br>
   * <pre>
   * @org.junit.jupiter.api.BeforeAll
   * public static void onlyOnce() {
   *   TestUtilRestDoc.beforeClass();
   * }
   * </pre>
   */
  public static void beforeClass() {
    ConstraintDescriptions problemConstraints = new ConstraintDescriptions(DefaultProblemEx.class);
    problemFields = new FieldDescriptor[] {
        //fieldWithPath("cause").type(JsonFieldType.STRING).description("cause")
        //  .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("cause")))),
        
        fieldWithPath("stacktrace").type(JsonFieldType.ARRAY).ignored().optional(),
        fieldWithPath("stacktrace.methodName").type(JsonFieldType.STRING).ignored().optional(),
        fieldWithPath("stacktrace.fileName").type(JsonFieldType.STRING).ignored().optional(),
        fieldWithPath("stacktrace.lineNumber").type(JsonFieldType.STRING).ignored().optional(),
        fieldWithPath("stacktrace.className").type(JsonFieldType.STRING).ignored().optional(),
        fieldWithPath("stacktrace.nativeMethod").type(JsonFieldType.STRING).ignored().optional(),

        fieldWithPath("type").type(JsonFieldType.STRING).description(" A URI reference [RFC3986] that identifies the problem type")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("type")))),
        fieldWithPath("title").type(JsonFieldType.STRING).description("summary of the problem type")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("title")))),
        fieldWithPath("status").type(JsonFieldType.NUMBER).description("The HTTP status code ([RFC7231], Section 6) generated by the origin server for this occurrence of the problem")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("status")))),
        fieldWithPath("detail").type(JsonFieldType.STRING).description("A human-readable explanation specific to this occurrence of the problem").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("detail")))),
        fieldWithPath("instance").type(JsonFieldType.STRING).optional().description("A URI reference that identifies the specific occurrence of the problem.  It may or may not yield further information if dereferenced")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("instance")))),
        fieldWithPath("path").type(JsonFieldType.STRING).description("problem path request ")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("path")))),
        fieldWithPath("message").type(JsonFieldType.STRING).description("i18n error code message").ignored().optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(problemConstraints.descriptionsForProperty("message")))),
          fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("List of errors detected in request payload").optional()
          .attributes(key("constraints").value("")),
        fieldWithPath("fieldErrors[].objectName").type(JsonFieldType.STRING).description("json object which validation fails").optional()
          .attributes(key("constraints").value("")),
        fieldWithPath("fieldErrors[].field").type(JsonFieldType.STRING).description("field which fail validation").optional()
          .attributes(key("constraints").value("")),
        fieldWithPath("fieldErrors[].message").type(JsonFieldType.STRING).description("validation of error field").optional()
          .attributes(key("constraints").value("")),
        fieldWithPath("fieldErrors[].code").type(JsonFieldType.STRING).description("code to retrieve a translation in the frontend").optional()
          .attributes(key("constraints").value("")),
    };
    
    ConstraintDescriptions networkTestRequestConstraints = new ConstraintDescriptions(NetworkTestRequest.class);
    ConstraintDescriptions networkTestResponseConstraints = new ConstraintDescriptions(NetworkTestResponse.class);
    
    defaultFields = new FieldDescriptor[] { 
        fieldWithPath("msg").type(JsonFieldType.STRING).description("echo message")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(networkTestRequestConstraints.descriptionsForProperty("message")))), };
      networkTestRequestFields = ArrayUtils.addAll(new FieldDescriptor[] {}, defaultFields);
      networkTestResponseFields = ArrayUtils.addAll(new FieldDescriptor[] {}, defaultFields);

      dateTime = fieldWithPath("dateTime").type(JsonFieldType.VARIES).description("Date Time of response")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(networkTestResponseConstraints.descriptionsForProperty("dateTime"))));
      networkTestResponseFields = ArrayUtils.addAll(networkTestResponseFields, dateTime);
    
    simpleResponseConstraints = new ConstraintDescriptions(SimpleResponse.class);
    
    simpleRespFields = new FieldDescriptor[] { 
      fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("true for success, false otherwise")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("success")))), 
      fieldWithPath("eid").type(JsonFieldType.NUMBER).description("error ID, success is 0")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("errorId")))), 
      fieldWithPath("msg").type(JsonFieldType.STRING).description("error/success message").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("message")))), 
    };
    
    simpleRespFieldsWithData = new FieldDescriptor[] { 
        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("true for success, false otherwise")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("success")))), 
        fieldWithPath("eid").type(JsonFieldType.NUMBER).description("error ID, success is 0")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("errorId")))), 
        fieldWithPath("msg").type(JsonFieldType.STRING).description("error/success message").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("message")))), 
        fieldWithPath("data").type(JsonFieldType.OBJECT).description("data payload").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("data")))), 
      };
    
    ConstraintDescriptions simpleRequestConstraints = new ConstraintDescriptions(SimpleRequest.class);
    simpleRequestFields = new FieldDescriptor[] {
        fieldWithPath("data").type(JsonFieldType.VARIES).description("data request").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleRequestConstraints.descriptionsForProperty("data")))),
      };
    ConstraintDescriptions userDtoConstraints = new ConstraintDescriptions(UserDto.class);
    
    userRequestFields= new FieldDescriptor[] {
      fieldWithPath("login").type(JsonFieldType.STRING).description("user id or login")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("login")))), 
      fieldWithPath("firstName").type(JsonFieldType.STRING).description("first Name")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("firstName")))), 
      fieldWithPath("lastName").type(JsonFieldType.STRING).description("last Name")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("lastName")))), 
      fieldWithPath("email").type(JsonFieldType.STRING).description("email")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("email")))), 
      fieldWithPath("activated").type(JsonFieldType.BOOLEAN).description("true to activate").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("activated")))), 
      fieldWithPath("password").type(JsonFieldType.STRING).description("password")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("password")))),
      fieldWithPath("langKey").type(JsonFieldType.STRING).description("langKey")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("langKey")))),
      fieldWithPath("authorities").type(JsonFieldType.ARRAY).description("role user").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("authorities")))), 
    
    }; 
    
    userRespFields = new FieldDescriptor[] {
      fieldWithPath("user.login").type(JsonFieldType.STRING).description("user id or login")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("login")))), 
      fieldWithPath("user.firstName").type(JsonFieldType.STRING).description("first Name")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("firstName")))), 
      fieldWithPath("user.lastName").type(JsonFieldType.STRING).description("last Name")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("lastName")))), 
      fieldWithPath("user.email").type(JsonFieldType.STRING).description("email")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("email")))), 
      fieldWithPath("user.activated").type(JsonFieldType.BOOLEAN).description("true to activate").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("activated")))), 
      fieldWithPath("user.password").type(JsonFieldType.STRING).description("password").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("password")))),
      fieldWithPath("user.langKey").type(JsonFieldType.STRING).description("langKey")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("langKey")))),
        fieldWithPath("user.company").type(JsonFieldType.STRING).description("Company")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("company")))),
      fieldWithPath("user.authorities").type(JsonFieldType.ARRAY).description("role user").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("authorities")))), 
    };
    
    userRespDataFields = new FieldDescriptor[] {
        fieldWithPath("data.login").type(JsonFieldType.STRING).description("user id or login")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("login")))), 
        fieldWithPath("data.firstName").type(JsonFieldType.STRING).description("first Name")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("firstName")))), 
        fieldWithPath("data.lastName").type(JsonFieldType.STRING).description("last Name")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("lastName")))), 
        fieldWithPath("data.email").type(JsonFieldType.STRING).description("email")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("email")))), 
        fieldWithPath("data.activated").type(JsonFieldType.BOOLEAN).description("true to activate").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("activated")))), 
        fieldWithPath("data.password").type(JsonFieldType.STRING).description("password").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("password")))),
        fieldWithPath("data.langKey").type(JsonFieldType.STRING).description("langKey")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("langKey")))),
          fieldWithPath("data.company").type(JsonFieldType.STRING).description("Company")
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("company")))),
        fieldWithPath("data.authorities").type(JsonFieldType.ARRAY).description("role user").optional()
          .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("authorities")))), 
      };
    
    userReqFields = new FieldDescriptor[] {
      fieldWithPath("login").type(JsonFieldType.STRING).description("user id or login")
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("login")))), 
        fieldWithPath("password").type(JsonFieldType.STRING).description("user password").optional()
        .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(userDtoConstraints.descriptionsForProperty("password")))),
    };
    
  }//

  private static final ObjectMapper mapper = createObjectMapper();
  
  /** MediaType for JSON UTF8 */
  public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON_UTF8;
  
  private static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }
  
  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert.
   * @return the JSON byte array.
   * @throws IOException
   */
  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    return mapper.writeValueAsBytes(object);
  }

  /**
   * reset {@link FieldDescriptor} for {@link SimpleResponse} data parameterized property
   * @param fieldType {@link JsonFieldType}
   * @param description to document
   * @param optional true if optional field
   * @return new {@value #simpleRespFieldsWithData} with data field redefined
   */
  public static FieldDescriptor[] replaceSimpleRespFieldsData(JsonFieldType fieldType, String description, boolean optional) {
    FieldDescriptor fd = fieldWithPath("data").type(fieldType).description(description)
    .attributes(key("constraints").value(Joiner.on(", ").skipNulls().join(simpleResponseConstraints.descriptionsForProperty("data"))));
    if (optional)
      fd = fd.optional();
    FieldDescriptor[]  respFieldsWithData = ArrayUtils.clone(simpleRespFieldsWithData); 
    respFieldsWithData = ArrayUtils.remove(respFieldsWithData, respFieldsWithData.length - 1);
    respFieldsWithData = ArrayUtils.add(respFieldsWithData, fd);
    return respFieldsWithData;
  }
}
