package com.dbs.lib.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO representing a user, with his authorities.
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

  @NotNull
  //@Pattern(regexp = SecurityUtils.LOGIN_REGEX)
  @Size(min = 1, max = 50)
  private String login;

  @Size(max = 50)
  private String firstName;

  @Size(max = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 100)
  private String email;

  private boolean activated = false;
  
  @Size(min = 5, max = 40)
  private String password;

  @Size(min = 2, max = 5)
  private String langKey;
  
  private String company;

  private Set<String> authorities = new HashSet<>();
  
  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();


  /**
   * 
   * @param login to set
   * @param firstName to set
   * @param lastName to set
   * @param email to set
   * @param activated to set
   * @param langKey to set
   * @param authorities to set
   */
  public UserDto(String login, String firstName, String lastName, String email, boolean activated, String langKey, Set<String> authorities) {
    this.login = login;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.activated = activated;
    this.langKey = langKey;
    this.authorities = authorities;
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
}
