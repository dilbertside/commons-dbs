package com.dbs.lib.test;


import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public abstract class TestPojoUtils {

  private static Validator validator = null;

  public static Validator getValidator() {
    if (null == validator) {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      return factory.getValidator();
    }
    return validator;
  }

  public static boolean isSerializable(final Class<?> c) {
    return (Serializable.class.isAssignableFrom(c));
  }

  public static boolean isSerializable(final Object c) {
    return isSerializable(c.getClass());
  }

  /**
   * 
   * @param pojo object to validate
   * @return list of constraint violations
   */
  public static <T> List<String> validate(T pojo) {
    List<String> list = new ArrayList<>();
    if (null == pojo)
      return list;
    Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(pojo);
    for (ConstraintViolation<T> constraintViolation : constraintViolations) {
      list.add(String.format("%s %s", constraintViolation.getPropertyPath(), constraintViolation.getMessage()));
    }
    return list;
  }
  
  static public <T> void validateAssert(T pojo) {
    List<String> list = TestPojoUtils.validate(pojo);
    boolean errors = false;
    for (String string : list) {
      System.err.println("validation errors for " + pojo.getClass().getCanonicalName() + ": " + string);
      errors = true;
    }
    assertFalse(errors, pojo.toString() + " pojo has errors, see log info");
  }
}
