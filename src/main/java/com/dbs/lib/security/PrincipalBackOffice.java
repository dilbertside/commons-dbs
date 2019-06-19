/**
 * PrincipalBackOffice
 */
package com.dbs.lib.security;

import java.security.Principal;

/**
 * @author dbs on Oct 1, 2018 4:08:39 PM
 * @since 1.0.0
 * @version 1.0
 *
 */
public class PrincipalBackOffice implements Principal {

  private final String name;
  
  public PrincipalBackOffice(String name) {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see java.security.Principal#getName()
   */
  @Override
  public String getName() {
    return name;
  }

}
