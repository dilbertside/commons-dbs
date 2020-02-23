/**
 * AbstractService
 */
package com.dbs.lib.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author dbs on Aug 22, 2016 6:42:09 PM
 * @since 1.0.0
 * @version 1.0
 * @version 1.1 return pair object to indicate if validation has errors or not
 * @version 1.2 add {@link #validate(Object, String, String)}
 *
 * @param <T> default entity to validate
 */
public class AbstractService<T> {

  @Autowired
  @Qualifier("mvcValidator")
  protected org.springframework.validation.Validator validator;

  /**
   * simple validator to replace for example when it is impossible to add BindingResult result as a parameter when ExtDirectMethodType is of type FORM_POST_JSON
   * 
   * @param toValidate bean to validate
   * @return BindingResult
   */
  protected BindingResult validateEntity(Object toValidate) {
    DataBinder binder = new DataBinder(toValidate);
    binder.setValidator(this.validator);
    ValidationUtils.invokeValidator(validator, toValidate, binder.getBindingResult());
    return binder.getBindingResult();
  }
  
  /**
   * the custom validator handler before running the object validator
   * 
   * @param toValidate object
   * @param objectDescription human readable object description
   * @param separator between different errors
   * @param handler {@link CustomValidationHandler} to reuse the current BindingResult for extra check
   * @return empty string if no errors
   */
  protected Pair<Boolean, String> validateEntity(Object toValidate, String objectDescription, String separator, CustomValidationHandler handler) {
    BindingResult result = buildBinder(toValidate);
    if (null != handler) {
      handler.validate(result, toValidate);
    }
    ValidationUtils.invokeValidator(validator, toValidate, result);
    return buildListErrors(objectDescription, separator, result);
  }
  
  /**
   * @param toValidate object
   * @param objectDescription human readable object name
   * @return pair of boolean key true if it has errors, and value empty list if no validation error
   */
  protected Pair<Boolean, List<String>> validateEntity(Object toValidate, String objectDescription) {
    return validateEntityAndCustom(toValidate, objectDescription, null);
  }

  /**
   * build a concatenated string from an object to validate
   * 
   * @param toValidate entity
   * @param objectDescription human readable object description
   * @param separator separator to use between each error
   * @return pair of boolean key true if it has errors, and value empty string if no errors
   */
  protected Pair<Boolean, String> validateEntity(Object toValidate, String objectDescription, String separator) {
    return validateEntity(toValidate, objectDescription, separator, null);
  }
  
  /**
   * build a concatenated string from the service paramaterized object to validate
   * @param toValidate T
   * @param objectDescription  human readable object description
   * @param separator separator to use between each error
   * @return pair of boolean key true if it has errors, and value empty string if no errors
   */
  public Pair<Boolean, String> validate(T toValidate, String objectDescription, String separator) {
    return validateEntity(toValidate, objectDescription, separator, null);
  }
  
  /**
   * build a concatenated string from the service paramaterized object to validate
   * @param toValidate T
   * @param objectDescription  human readable object description
   * @param separator separator to use between each error
   * @return pair of boolean key true if it has errors, and value empty string if no errors
   */
  public  <T> void  validate(T toValidate) throws ConstraintViolationException {
    BindingResult bindingResult = validateEntity(toValidate);
    if (bindingResult.hasErrors()) {
      Set<ConstraintViolation<T>> constraintViolations = new HashSet<ConstraintViolation<T>>();
      for (ObjectError objectError : bindingResult.getAllErrors()) {
        if (objectError instanceof FieldError) {
          FieldError fe = (FieldError) objectError;
          ConstraintViolation<T> cv = new ConstraintViolationImpl<T>(fe);
          constraintViolations.add(cv);
        } else {
          // TODO
        }
      }
      for (ConstraintViolation<?> constraintViolation : constraintViolations) {
        
      }
      throw new ConstraintViolationException(String.format("Failed validation for %s", toValidate), constraintViolations );
    }
  }
  
  /**
   * build a concatenated string from a binding result list of errors
   * 
   * @param objectDescription human readable object description
   * @param separator separator to use between each error
   * @param bindingResult {@link BindingResult}
   * @return pair of boolean key true if it has errors, and value empty string if no errors
   */
  public Pair<Boolean, String> buildListErrors(String objectDescription, String separator, BindingResult bindingResult) {
    Pair<Boolean, List<String>> pair = buildListErrors(objectDescription, bindingResult);
    return Pair.of(pair.getKey(), Joiner.on(separator).skipNulls().join(pair.getValue()));
  }

  /**
   * return a list of errors string if any from a binding result
   * 
   * @param objectDescription {@link String}
   * @param bindingResult {@link BindingResult}
   * @return pair of boolean key true if it has errors, and value empty list if no errors
   */
  protected Pair<Boolean, List<String>> buildListErrors(String objectDescription, BindingResult bindingResult) {
    List<String> errors = Lists.newArrayList();
    boolean hasErrors = false;
    if (bindingResult.hasErrors()) {
      hasErrors = false;
      errors.add("Validation failed for ");
      for (ObjectError objectError : bindingResult.getAllErrors()) {
        if (objectError instanceof FieldError) {
          FieldError fe = (FieldError) objectError;
          errors.add(format("Object (%s) for property [%s] with value [%s] failed with validation error %s%n", objectDescription, fe.getField(),
              fe.getRejectedValue(), objectError.getDefaultMessage()));
        } else {
          errors.add(format("Object (%s) for property [%s] failed with validation error [%s]%n", objectDescription, objectError.getObjectName(),
              objectError.getDefaultMessage()));
        }
      }
    }
    return Pair.of(hasErrors, errors);
  }

  /**
   * return a list of errors string from an object to validate
   * 
   * @param toValidate entity
   * @param objectDescription human readable object description
   * @param handler {@link CustomValidationHandler} to reuse the current BindingResult for extra check
   * @return pair of boolean key true if it has errors, and value pair of  list of strings
   */
  protected Pair<Boolean, List<String>> validateEntityAndCustom(Object toValidate, String objectDescription, CustomValidationHandler handler) {
    BindingResult result = buildBinder(toValidate);
    if (null != handler) {
      handler.validate(result, toValidate);
    }
    ValidationUtils.invokeValidator(validator, toValidate, result);
    return buildListErrors(objectDescription, result);
  }

  protected BindingResult buildBinder(Object toValidate) {
    DataBinder binder = new DataBinder(toValidate);
    binder.setValidator(this.validator);
    return binder.getBindingResult();
  }

  /**
   * utility to copy errors from a binder to target binder
   * 
   * @param result target to receive error if any
   * @param toClone errors to copy, if result, toClone will be returned
   */
  protected BindingResult addAllErrors(@Nullable BindingResult result, @Nullable final BindingResult toClone) {
    if (null == result) {
      return toClone;
    }
    if (toClone != null) {
      for (ObjectError objectError : toClone.getAllErrors()) {
        result.addError(objectError);
      }
    }
    return result;
  }

  /**
   * @author dbs on May 22, 2014 8:29:07 PM
   * @version 1.0
   * @since V1.0.0
   *
   */
  public interface CustomValidationHandler {
    /**
     * @param result {@link BindingResult}
     * @param toValidate to set
     */
    void validate(final BindingResult result, final Object toValidate);
  }
}
