/**
 * OnDependencyCondition
 */
package com.dbs.lib.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

/**
 * {@link Condition} that matches based on the value of a dependency is included.
 *
 * @author dbs
 * @since 1.0.6
 */
public class OnDependencyCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(ConditionalOnDependency.class.getName());
    if (attrs != null) {
      String[] array;
      boolean[] presents = null;
      for (Object value : attrs.get("value")) {
        array = (String[]) value;
        presents = new boolean[array.length];
        for (int i = 0; i < presents.length; i++) {
          presents[i] = ClassUtils.isPresent(array[i], context.getClassLoader());
        }
      }
      return !ArrayUtils.contains(presents, false);
    }
    return false;
  }

}
