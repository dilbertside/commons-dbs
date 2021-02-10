package com.dbs.lib.jpa;

import javax.persistence.EntityManager;

/**
 * 
 * @author dbs on Aug 15, 2017 9:42:24 AM
 * @since 0.2.15
 * @version 1.0
 *
 */
public class JpaQueryFactory extends com.querydsl.jpa.impl.JPAQueryFactory {

  private final EntityManager entityManager;

  public JpaQueryFactory(EntityManager entityManager) {
    super(entityManager);
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager() {
    return this.entityManager;
  }

}
