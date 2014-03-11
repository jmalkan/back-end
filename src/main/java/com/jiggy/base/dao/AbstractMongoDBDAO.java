package com.jiggy.base.dao;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.SearchCriteria;

/**
 * <p>
 * Data access layer base class for MongoDB.
 * 
 * @see AbstractDBDAO<T>
 * 
 * @author jmalkan
 */
public abstract class AbstractMongoDBDAO<T extends Entity> extends AbstractDBDAO<T> {
  private MongoOperations mongoOperations;
  
  
  /**
   * Creates a new instance of com.jiggy.base.dao.AbstractHibernateDBDAO.java and Performs Initialization
   * 
   * @param mongoOperations The Spring Data instance of mongoOperations this dao interacts with.
   */
  public AbstractMongoDBDAO(final MongoOperations mongoOperations) {
    super();
    this.mongoOperations = mongoOperations;
  }
  
  /**
   * Implements logic to find an entity for the given id.
   * 
   * @param id The id of the entity being searched.
   * @return entity An instance of the Entity that is retrieved from the data store.
   */
  protected T implementFindById(final Long id) {
    return (T) this.mongoOperations.findById(id, super.getPersistentClass());
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
    List<T> foundEntities = null;
    
    if (searchCriteria == null)
      foundEntities = this.mongoOperations.findAll(super.getPersistentClass());
    
    return foundEntities;
  }

  @Override
  protected long implementRowCount(final SearchCriteria searchCriteria) {
    long rowCout = 0;
    
    if (searchCriteria == null)
      rowCout = this.mongoOperations.count(null, super.getPersistentClass());
    
    return rowCout;
  }
  
  /**
   * Implements logic to persist the entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  protected T implementInsert(final T entity) {
    this.mongoOperations.insert(entity);
    
    return this.findById(entity.getId());
  }
  
  /**
   * Implements logic to persist the modified entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  protected T implementUpdate(final T entity) {
    this.mongoOperations.save(entity);
    
    return this.findById(entity.getId());
  }
  
  /**
   * Implements logic to delete an persisted entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  protected void implementDelete(final T entity) {
    this.mongoOperations.remove(entity);
  }
}