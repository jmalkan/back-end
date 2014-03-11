package com.jiggy.base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.SearchCriteria;

/**
 * <p>
 * Data access layer base class for Hibernate.
 * 
 * @see AbstractDBDAO<T>
 * 
 * @author jmalkan
 */
public abstract class AbstractHibernateDBDAO<T extends Entity> extends AbstractDBDAO<T> {
  private SessionFactory sessionFactory;
  
  
  /**
   * Creates a new instance of com.jiggy.base.dao.AbstractHibernateDBDAO.java and Performs Initialization
   * 
   * @param sessionFactory The Hibernate's sessionFactory Object this dao interacts with.
   */
  public AbstractHibernateDBDAO(final SessionFactory sessionFactory) {
    super();
    this.sessionFactory = sessionFactory;
  }
  
  
  /**
   * @return The current session from Hibernate's SessionFactory
   */
  protected final Session getCurrentSession() {
    return this.sessionFactory.getCurrentSession();
  }
  
  /**
   * Implements logic to find an entity for the given id.
   * 
   * @param id The id of the entity being searched.
   * @return entity An instance of the Entity that is retrieved from the data store.
   */
  @SuppressWarnings("unchecked")
  protected T implementFindById(final Long id) {
    return (T) this.getCurrentSession().get(super.getPersistentClass(), id);
  }
  
  /**
   * Implements logic to find all the entities that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * @return List of entity objects that matches the search criteria
   * 
   * @see SearchCriteria
   */
  protected List<T> implementFind(SearchCriteria searchCriteria) {
    List<T> result = null; //this.getCurrentSession().loadAll(this.getEntity());
    
    return result;
  }

  @Override
  protected long implementRowCount(final SearchCriteria searchCriteria) {
    return 0;
  }
  
  /**
   * Implements logic to persist the entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  @SuppressWarnings("unchecked")
  protected T implementInsert(final T entity) {
    return (T) this.getCurrentSession().save(entity);
  }
  
  /**
   * Implements logic to persist the modified entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  protected T implementUpdate(final T entity) {
    this.getCurrentSession().update(entity);
    return entity;
  }
  
  /**
   * Implements logic to delete an persisted entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  protected void implementDelete(final T entity) {
	  this.getCurrentSession().delete(entity);
  }
}