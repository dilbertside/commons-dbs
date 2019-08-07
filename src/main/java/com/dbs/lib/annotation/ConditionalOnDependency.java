/**
 * ConditionalOnDependency
 */
package com.dbs.lib.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * @author dbs
 * @since 1.0.6
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Conditional(OnDependencyCondition.class)
public @interface ConditionalOnDependency {

  /**
   * The classes in a dependency which must be present.<br>
   * example: <b>org.h2.server.web.WebServlet</b>
   */
  String[] value();
}
