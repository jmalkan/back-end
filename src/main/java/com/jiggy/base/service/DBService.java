package com.jiggy.base.service;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.SearchCriteria;

import java.util.List;

/**
 * <p>
 * Business layer facade base class for a standard Services interacting with single DAO.
 * </p>
 * 
 * @param <T> An Object that implements Entity interface.
 * 
 * @author jmalkan
 */
public interface DBService<T extends Entity> extends Service {
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
   * Finds the entity for the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input.
   * @return List of entity objects that matches the search criteria.
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
  int getRowCount();
  
  /**
   * Returns the count of such entities.
   * 
   * @param searchCriteria
   * @return The row count
   */
  int getRowCount(final SearchCriteria searchCriteria);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the domain/entity object.
   * 
   * @return The Id of the newly created entity.
   */
  T insert(final T entity);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the domain/entity object.
   * @return The modified entity.
   */
  T update(final T entity);
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param entity An instance of the domain/entity object that needs to be deleted
   */
  void delete(final T entity);
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param id The id of the domain/entity object that needs to be deleted.
   */
  void delete(final Long id);
  
  /**
   * This operation can span across multiple DAO's or Services or combinations.
   * 
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entities List of entities of the domain/entity object.
   * 
   * @return List of newly created entities.
   */
  List<? extends Entity> create(final List<? extends Entity> entities);
  
  /**
   * This operation can span across multiple DAO's or Services or combinations.
   * 
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entities List of entities of the domain/entity object.
   * @return List of modified entities.
   */
  List<? extends Entity> modify(final List<? extends Entity> entities);
  
  /**
   * This operation can span across multiple DAO's or Services or combinations.
   * 
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param entities List of entities of the domain/entity object.
   */
  void remove(final List<? extends Entity> entities);
}