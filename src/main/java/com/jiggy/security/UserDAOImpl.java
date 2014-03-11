package com.jiggy.security;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiggy.base.dao.AbstractHibernateDBDAO;

/**
 * Data access implementation for UserCredentials.
 * 
 * @author jmalkan
 */
@Repository("userDAO")
public class UserDAOImpl extends AbstractHibernateDBDAO<User> implements UserDAO {
  /**
   * Creates a new instance of com.jiggy.security.UserDAOImpl.java and Performs Initialization
   * 
   * @param sessionFactory The Hibernate's sessionFactory Object this dao interacts with.
   */
  @Autowired
  public UserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}