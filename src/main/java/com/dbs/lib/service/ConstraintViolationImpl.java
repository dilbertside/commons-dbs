/**
 * ConstraintViolationImpl
 */
package com.dbs.lib.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

import org.springframework.validation.FieldError;

/**
 * @author dbs at 23 Aug 2019 15:59:23
 * @since 1.0.0
 * @version 1.0
 */
public class ConstraintViolationImpl<T> extends FieldError implements ConstraintViolation<T>, Serializable {

  private static final long serialVersionUID = -3700046385584152714L;

  private final Class<T> persistentClass;
  /**
   * @param objectName
   * @param field
   * @param rejectedValue
   * @param bindingFailure
   * @param codes
   * @param arguments
   * @param defaultMessage
   */
  public ConstraintViolationImpl(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes, Object[] arguments,
      String defaultMessage) {
    super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
    this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * @param objectName
   * @param field
   * @param defaultMessage
   */
  public ConstraintViolationImpl(String objectName, String field, String defaultMessage) {
    this(objectName, field, null, false, null, null, defaultMessage);
  }

  public ConstraintViolationImpl(FieldError fe) {
    this(fe.getObjectName(), fe.getField(), fe.getRejectedValue(), fe.isBindingFailure(), fe.getCodes(), fe.getArguments(), fe.getDefaultMessage());
  }

  @Override
  public String getMessage() {
    return getDefaultMessage();
  }

  @Override
  public String getMessageTemplate() {
    return null;
  }

  @Override
  public T getRootBean() {
    return null;
  }

  @Override
  public Class<T> getRootBeanClass() {
    return persistentClass;
  }

  @Override
  public Object getLeafBean() {
    return null;
  }

  @Override
  public Object[] getExecutableParameters() {
    return null;
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Path getPropertyPath() {
    return null;
  }

  @Override
  public Object getInvalidValue() {
    return getRejectedValue();
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return null;
  }

  @Override
  public <U> U unwrap(Class<U> type) {
    return null;
  }

}
