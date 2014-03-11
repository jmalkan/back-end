package com.jiggy.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jiggy.base.entity.Entity;

/**
 * Defines basic REST data access CRUD operations. All the Controller classes will implement this interface.
 * 
 * @param <T> An Object that implements Entity interface.
 * 
 * @author jmalkan
 */
public interface DBController<T extends Entity> extends Controller {
  /**
   * Finds the entity for the given Id.
   * 
   * @param id The id for the entity to be searched for.
   * @return An instance of the entity object.
   */
  T findById(Long id);
  
  /**
   * Finds all the entity objects.
   * 
   * @return List of all the entity objects.
   */
  List<T> findAll();
  
  /**
   * Finds all the entities that matches the given search criteria with any user input for filter, sort, pagination.
   * User input is expected as name=value pairs seperated by "&"
   * Example : http://localhost:8080/service/../find/fName=Jiggy&lName=Malkan
   * 
   * @param request The Http request.
   * 
   * @return List of entity objects that matches the search criteria.
   */
  List<T> find(HttpServletRequest request);
  
  /**
   * Convenient method when one result is expected from the Search.
   * 
   * @see DBController#find(HttpServletRequest request)
   */
  T findOne(HttpServletRequest request);
  
  /**
   * Returns the count of such entities.
   * 
   * @return The row count
   */
  int getRowCount();
  
  /**
   * Returns the count of such entities.
   * 
   * @param request The Http request.
   * 
   * @return The row count.
   */
  int getRowCount(HttpServletRequest request);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  T create(final T entity);
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  T modify(final Long id, T entity);
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  void remove(final Long id, final T entity);
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured.
   * 
   * @param id An id of the entity object that needs to be deleted.
   */
  void remove(final Long id);
}