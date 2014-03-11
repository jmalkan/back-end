package com.jiggy.base.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.DefaultSearchCriteria;
import com.jiggy.base.searchengine.FilterExpression.FilterTerm;
import com.jiggy.base.searchengine.SearchCriteria;
import com.jiggy.base.service.DBService;

/**
 * AbstractResource.java
 * 
 * <p>
 * Controller layer facade base class with common functionality and extension points for the descendants. Implements DBController interface and
 * provides base implementation for the methods that could be extended by the descendant class. It calls Before and After abstract/empty methods for
 * additional logic during create, modify, and delete which descendants needs to provide implementation. This is not where business or data validation
 * logic should be implemented.
 * 
 * @author jmalkan
 * 
 * @param <T> The Entity object used for transformation.
 * 
 * @see com.jiggy.base.controller.DBController
 */
public abstract class AbstractController<T extends Entity> implements DBController<T> {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private DBService<T> service = null;
  
  /**
   * Creates a new instance of com.jiggy.base.AbstractController.java and Performs Initialization
   * 
   * @param service The Service that DBResource interacts with.
   */
  public AbstractController(final DBService<T> service) {
    super();
    this.service = service;
  }
  
  /**
   * Getter of the property <tt>service</tt>
   * 
   * @return the service
   */
  public DBService<T> getService() {
    return this.service;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public T findById(@PathVariable final Long id) {
    this.beforeFindById(id);
    T foundEntity = this.implementFindById(id);
    this.afterFindById(id, foundEntity);
    
    return foundEntity;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET)
  public List<T> findAll() {
    this.beforeFindAll();
    List<T> foundEntities = this.implementFindAll();
    this.afterFindAll(foundEntities);
    
    return foundEntities;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public List<T> find(final HttpServletRequest request) {
    this.beforeFind(request);
    List<T> foundEntities = this.implementFind(request);
    this.afterFind(foundEntities);
    
    return foundEntities;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/findOne", method = RequestMethod.GET)
  public T findOne(final HttpServletRequest request) {
    this.beforeFindOne(request);
    T foundEntity = this.implementFindOne(request);
    this.afterFindOne(foundEntity);
    
    return foundEntity;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/rowCount", method = RequestMethod.GET)
  public int getRowCount() {
    return this.implementRowCount();
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/rowcount", method = RequestMethod.GET)
  public int getRowCount(final HttpServletRequest request) {
    return this.implementRowCount(request);
  }
  
  @Override
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST)
  public T create(@RequestBody final T entity) {
    this.beforeCreate(entity);
    T savedEntity = this.implementCreate(entity);
    this.afterCreate(savedEntity);
    
    return savedEntity;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public T modify(@PathVariable final Long id, @RequestBody final T entity) {
    this.beforeModify(id, entity);
    T modifiedEntity = this.implementModify(id, entity);
    this.afterModify(modifiedEntity);
    
    return modifiedEntity;
  }
  
  @Override
  @ResponseBody
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void remove(@PathVariable final Long id) {
    this.beforeRemove(id);
    this.implementRemove(id);
    this.afterRemove(id);
  }
  
  @Override
  @ResponseBody
  @RequestMapping(method = RequestMethod.DELETE)
  //@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void remove(@PathVariable final Long id, @RequestBody final T entity) {
    this.beforeRemove(id, entity);
    this.implementRemove(id, entity);
    this.afterRemove(id, entity);
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   * 
   * @param id The id of the entity that is about to be accessed.
   */
  protected void beforeFindById(final Long id) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id The id for the entity to be searched for
   * @return If found, an instance of the entity object, else null.
   */
  protected T implementFindById(final Long id) {
    return this.getService().findById(id);
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param id The id received from the client.
   * @param foundEntity The entity found.
   */
  protected void afterFindById(final Long id, final T foundEntity) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   */
  protected void beforeFindAll() {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @return If found, a list of the entity object, else null.
   */
  protected List<T> implementFindAll() {
    return this.getService().findAll();
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param foundEntities contains list of found entities.
   */
  protected void afterFindAll(final List<T> foundEntities) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   */
  protected void beforeFind(final HttpServletRequest request) {
    return;
  }
  
  /**
   * The extending class may over-ride the implementation.
   * 
   * @param request The Http request.
   * 
   * @return If found, a list of the entities, else null.
   */
  protected List<T> implementFind(final HttpServletRequest request) {
    List<T> entities = null;
    SearchCriteria searchCriteria = this.buildSearchCriteria(request);
    
    if (searchCriteria != null) {
      entities = this.getService().find(searchCriteria);
      
      //int rowCount = (searchCriteria.getRowCount() < entities.size() ? entities.size() : searchCriteria.getRowCount());
    }
    
    return entities;
  }
  
  /**
   * Builds Search Criteria from the Query param.
   * 
   * @param request The Http request.
   * 
   * @return An instance of the Search Criteria.
   */
  @SuppressWarnings("unchecked")
  protected SearchCriteria buildSearchCriteria(final HttpServletRequest request) {
    Enumeration<String> parameterNames = request.getParameterNames();
    DefaultSearchCriteria defaultSearchCriteria = null;
    
    if (parameterNames != null) {
      while (parameterNames.hasMoreElements()) {
        String paramName = parameterNames.nextElement();
        String[] paramValues = request.getParameterValues(paramName);
        
        if (defaultSearchCriteria == null)
          defaultSearchCriteria = new DefaultSearchCriteria();
        
        if ("sort".equalsIgnoreCase(paramName))
          defaultSearchCriteria.setSortBy(paramValues[0]);
        else if ("offset".equalsIgnoreCase(paramName))
          defaultSearchCriteria.setOffset(Integer.parseInt(paramValues[0]));
        else if ("limit".equalsIgnoreCase(paramName))
          defaultSearchCriteria.setLimit(Integer.parseInt(paramValues[0]));
        else
          defaultSearchCriteria.addFilter(new FilterTerm(paramName, paramValues[0]));
      }
    }
    
    return defaultSearchCriteria;
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param foundEntities contains list of found entities.
   */
  protected void afterFind(final List<T> entities) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   * 
   * @param request The Http request.
   */
  protected void beforeFindOne(final HttpServletRequest request) {
    return;
  }
  
  /**
   * The extending class may over-ride the implementation.
   * 
   * @param request The Http request.
   * 
   * @return If found, the first entity from the list of the entities, else null.
   */
  protected T implementFindOne(final HttpServletRequest request) {
    List<T> foundEntities = this.find(request);
    
    return (foundEntities != null && !foundEntities.isEmpty() ? foundEntities.get(0) : null);
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param foundEntity The entity found.
   */
  protected void afterFindOne(final T foundEntity) {
    return;
  }
  
  /**
   * Implements logic to get the count of rows.
   */
  protected int implementRowCount() {
    return this.getService().getRowCount();
  }
  
  /**
   * Implements logic to get the count of rows that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * 
   * @see SearchCriteria
   */
  protected int implementRowCount(final HttpServletRequest request) {
    return this.getService().getRowCount(this.buildSearchCriteria(request));
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is created in the data store.
   * 
   * @param entity capturing user input.
   */
  protected void beforeCreate(final T entity) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param entity capturing user input.
   */
  protected T implementCreate(final T entity) {
    return this.getService().insert(entity);
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is created in the data store.
   * 
   * @param entity The saved entity.
   */
  protected void afterCreate(final T savedEntity) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is updated in the data store.
   * 
   * @param id The id of the entity to be updated.
   * @param entity capturing user input.
   */
  protected void beforeModify(final Long id, final T entity) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id The id of the entity to be updated.
   * @param entity capturing user input.
   */
  protected T implementModify(final Long id, final T entity) {
    return this.getService().update(entity);
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is updated in the data store.
   * 
   * @param entity The modified entity.
   */
  protected void afterModify(final T modifiedEntity) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is deleted from the data store.
   * 
   * @param id the Id of the entity to be removed.
   */
  protected void beforeRemove(final Long id) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id the Id of the entity to be removed.
   */
  protected void implementRemove(final Long id) {
    this.getService().delete(id);
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is deleted from the data store.
   * 
   * @param id the Id of the entity to be removed.
   */
  protected void afterRemove(final Long id) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is deleted from the data store.
   * 
   * @param id the Id of the entity to be removed.
   * @param entity to be removed.
   */
  protected void beforeRemove(final Long id, final T entity) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id the Id of the entity to be removed.
   * @param entity to be removed.
   */
  protected void implementRemove(final Long id, final T entity) {
    this.getService().delete(entity);
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is deleted from the data store.
   * 
   * @param id the Id of the entity to be removed.
   * @param removedEntity removed entity.
   */
  protected void afterRemove(final Long id, final T removedEntity) {
    return;
  }
}