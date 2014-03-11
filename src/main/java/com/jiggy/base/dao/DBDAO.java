package com.jiggy.base.dao;

import java.util.List;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.SearchCriteria;

/**
 * Defines basic data access CRUD operations. All the DAO/Data access classes will implement this interface.
 * 
 * @param <T> An Object that implements Entity interface.
 * 
 * @author jmalkan
 */
public interface DBDAO<T extends Entity> extends DAO {
  /**
   * Finds the entity for the given Id.
   * 
   * @param id The id for the entity to be searched for.
   * @return An instance of the entity object.
   */
  T findById(final Long id);
  
  /**
   * Finds all the entity objects.
   * 
   * @return List of all the entity objects.
   */
  List<T> findAll();
  
  /**
   * Finds all the entities that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input.
   * @return List of entity objects that matches the search criteria.
   * 
   * @see SearchCriteria
   */
  List<T> find(final SearchCriteria searchCriteria);
  
  /**
   * Convenient method when one result is expected from the Search. Finds the entities that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input.
   * @return An instance of the entity object.
   * 
   * @see SearchCriteria
   */
  T findOne(final SearchCriteria searchCriteria);
  
  /**
   * Returns the count of such entities.
   * 
   * @return The row count
   */
  long getRowCount();
  
  /**
   * Returns the count of such entities.
   * 
   * @param searchCriteria
   * @return The row count
   */
  long getRowCount(final SearchCriteria searchCriteria);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  T insert(final T entity);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  T update(final T entity);
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  void delete(final T entity);
}