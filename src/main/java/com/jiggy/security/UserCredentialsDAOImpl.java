package com.jiggy.security;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiggy.base.dao.AbstractHibernateDBDAO;
import com.jiggy.base.searchengine.SearchCriteria;
import com.jiggy.base.searchengine.FilterExpression.FilterTerm;

/**
 * Data access implementation for UserCredentials.
 * 
 * @author jmalkan
 */
@Repository("userCredentialsDAO")
public class UserCredentialsDAOImpl extends AbstractHibernateDBDAO<UserCredentials> implements UserCredentialsDAO {
  /**
   * Creates a new instance of com.jiggy.security.UserCredentialsDAOImpl.java and Performs Initialization
   * 
   * @param sessionFactory The Hibernate's sessionFactory Object this dao interacts with.
   */
  @Autowired
  public UserCredentialsDAOImpl(final SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  protected List<UserCredentials> implementFind(final SearchCriteria searchCriteria) {
    String queryString = "from " + this.getPersistentClass().getSimpleName() + " where user.userName = :userName";
    
    Query query = this.getCurrentSession().createQuery(queryString);
    
    Collection<FilterTerm> filterTerms = searchCriteria.getFilter().terms();
    
    for (FilterTerm filterTerm : filterTerms) {
      query.setString(filterTerm.getKey(), filterTerm.getValue());
    }
    
    List<UserCredentials> result = query.list();
    
    return result;
  }
}