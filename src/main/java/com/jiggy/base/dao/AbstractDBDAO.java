package com.jiggy.base.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.FilterExpression.FilterAdvice;
import com.jiggy.base.searchengine.FilterExpression.FilterTerm;
import com.jiggy.base.searchengine.FilterExpression;
import com.jiggy.base.searchengine.SearchCriteria;
import com.jiggy.security.Role;
import com.jiggy.security.SessionUtil;
import com.jiggy.security.UserProfile;

/**
 * <p>
 * Data access layer base class. Implements DAO interface and provides base implementation for the methods that could be extended by the descendant
 * class. DAO class should extend from this class. It calls Before and After abstract/empty methods for validation during insert, update, and delete
 * which descendants needs to provide implementation. Any entity specific database check like required, field length should be implemented here.
 * Business logic should be implemented in the service layer. If any of these methods throws an Runtime exception, the transaction will rolled back.
 * </p>
 * 
 * <p>
 * This is a "singleton" class. This means a singleton per Spring instance. The factory creates a single instance; there is no need for a private
 * constructor, static factory method etc as in the traditional implementation of the Singleton Design Pattern.
 * </p>
 * 
 * @param <T> An Object that implements Entity interface.
 * 
 * @author jmalkan
 */
public abstract class AbstractDBDAO<T extends Entity> implements DBDAO<T>, FilterAdvice {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  private Class<T> persistentClass;
  
  @SuppressWarnings("unchecked")
  public AbstractDBDAO() {
    this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }
  
  public Class<T> getPersistentClass() {
    return persistentClass;
  }
  
  @Override
  public final T findById(final Long id) {
    if (id == null)
      return null;
    
    T entity = this.implementFindById(id);
    
    this.afterFind(entity);
    
    return entity;
  }
  
  @Override
  public final List<T> findAll() {
    final List<T> entities = this.implementFindAll();
    
    if (entities != null) {
      for (final T entity : entities) {
        this.afterFind(entity);
      }
    }
    
    return entities;
  }
  
  @Override
  public final List<T> find(final SearchCriteria searchCriteria) {
    final List<T> entities = this.implementFind(searchCriteria);
    
    if (entities != null) {
      for (final T entity : entities) {
        this.afterFind(entity);
      }
    }
    
    return entities;
  }
  
  @Override
  public final T findOne(final SearchCriteria searchCriteria) {
    T entity = this.implementFindOne(searchCriteria);
    
    this.afterFind(entity);
    
    return entity;
  }
  
  @Override
  public long getRowCount() {
    return this.implementRowCount();
  }

  @Override
  public long getRowCount(final SearchCriteria searchCriteria) {
    return this.implementRowCount(searchCriteria);
  }
  
  @Override
  public final T insert(final T entity) {
    this.validateBeforeInsert(entity);
    this.beforeInsert(entity);
    T newEntity = this.implementInsert(entity);
    this.afterInsert(newEntity);
    this.validateAfterInsert(newEntity);
    
    return newEntity;
  }
  
  @Override
  public final T update(final T entity) {
    this.validateBeforeUpdate(entity);
    this.beforeUpdate(entity);
    T updatedEntity = this.implementUpdate(entity);
    this.afterUpdate(entity);
    this.validateAfterUpdate(entity);
    
    return updatedEntity;
  }
  
  @Override
  public final void delete(final T entity) {
    this.validateBeforeDelete(entity);
    this.beforeDelete(entity);
    this.implementDelete(entity);
    this.afterDelete(entity);
    this.validateAfterDelete(entity);
  }
  
  /**
   * Implements non-data/non-business validation logic after entity is retrieved from the data store.
   * 
   * @param entity An instance of the Entity that is retrieved from the data store.
   */
  protected void afterFind(final T entity) {
    return;
  }
  
  /**
   * Implements logic to find an entity for the given id.
   * 
   * @param id The id of the entity being searched.
   * @return entity An instance of the Entity that is retrieved from the data store.
   */
  protected T implementFindById(final Long id) {
    return null;
  }
  
  /**
   * Implements logic to find all the entities.
   * 
   * @return List of all entities retrieved from the data store.
   */
  protected List<T> implementFindAll() {
    return this.find(null);
  }
  
  /**
   * Implements logic to find all the entities that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * @return List of entity objects that matches the search criteria
   * 
   * @see SearchCriteria
   */
  protected List<T> implementFind(final SearchCriteria searchCriteria) {
    return null;
  }
  
  /**
   * @param searchCriteria
   */
  protected void applySecurityFilter(final SearchCriteria searchCriteria) {
    UserProfile userProfile = SessionUtil.getUserProfile();
    
    if (userProfile != null) {
      String resourceName = this.getPersistentClass().getSimpleName();
      
      final Role role = userProfile.getRole();
      
      if (role == null) {
        logger.warn("UserProfile {} does not have an associated role.", userProfile);
        return;
      }
      
      String filterString = role.getPermissionFilter(resourceName, "read");
      searchCriteria.addFilter(filterString);
    }
  }
  
  /**
   * Implements logic to find the entity that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * @return The entity objects that matches the search criteria
   * 
   * @see SearchCriteria
   */
  protected T implementFindOne(final SearchCriteria searchCriteria) {
    T entity = null;
    final List<T> entities = this.find(searchCriteria);
    
    if (entities != null && entities.size() == 1) {
      entity = entities.get(0);
    }
    
    return entity;
  }
  
  /**
   * Implements logic to get the count of rows.
   */
  protected long implementRowCount() {
    return implementRowCount(null);
  }
  
  /**
   * Implements logic to get the count of rows that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * 
   * @see SearchCriteria
   */
  protected long implementRowCount(final SearchCriteria searchCriteria) {
    return 0;
  }
  
  /**
   * Implements data (non-business) validation logic before entity is inserted in the data store.
   * 
   * @param entity An instance of the Entity that is about to be created in the data store.
   */
  protected void validateBeforeInsert(final T entity) {
    return;
  }
  
  /**
   * Implements non-data/non-business validation logic before entity is inserted in the data store.
   * 
   * @param entity An instance of the Entity that is about to be created in the data store.
   */
  protected void beforeInsert(final T entity) {
    return;
  }
  
  /**
   * Implements logic to persist the entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  protected T implementInsert(final T entity) {
    return null;
  }
  
  /**
   * Implements non-data/non-business validation logic after entity is inserted in the data store.
   * 
   * @param newEntity The reference to the new Entity that is created in the data store.
   */
  protected void afterInsert(final T newEntity) {
    return;
  }
  
  /**
   * Implements business (non-data) validation logic after entity is inserted in the data store.
   * 
   * @param newEntity The reference to the new Entity that is created in the data store.
   */
  protected void validateAfterInsert(final T newEntity) {
    return;
  }
  
  /**
   * Implements data (non-business) validation logic before entity is updated in the data store.
   * 
   * @param entity An instance of the Entity that is about to be updated in the data store.
   */
  protected void validateBeforeUpdate(final T entity) {
    return;
  }
  
  /**
   * Implements non-data/non-business validation logic before entity is updated in the data store.
   * 
   * @param entity An instance of the Entity that is about to be updated in the data store.
   */
  protected void beforeUpdate(final T entity) {
    return;
  }
  
  /**
   * Implements logic to persist the modified entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  protected T implementUpdate(final T entity) {
    return null;
  }
  
  /**
   * Implements non-data/non-business validation logic after entity is updated in the data store.
   * 
   * @param entity An instance of the Entity that is updated in the data store.
   */
  protected void afterUpdate(final T entity) {
    return;
  }
  
  /**
   * Implements data (non-business) validation logic after entity is updated in the data store.
   * 
   * @param entity An instance of the Entity that is updated in the data store.
   */
  protected void validateAfterUpdate(final T entity) {
    return;
  }
  
  /**
   * Implements data (non-business) validation logic after entity is updated in the data store.
   * 
   * @param entity An instance of the Entity that is updated in the data store.
   */
  protected void validateBeforeDelete(final T entity) {
    return;
  }
  
  /**
   * Implements non-data/non-business validation logic before entity is deleted from the data store.
   * 
   * @param entity An instance of the Entity that is about to be deleted from the data store.
   */
  protected void beforeDelete(final T entity) {
    return;
  }
  
  /**
   * Implements logic to delete an persisted entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  protected void implementDelete(final T entity) {
    return;
  }
  
  /**
   * Implements non-data/non-business validation logic after entity is deleted from the data store.
   * 
   * @param entity An instance of the Entity that is deleted from the data store.
   */
  protected void afterDelete(final T entity) {
    return;
  }
  
  /**
   * Implements data (non-business) validation logic after entity is deleted from the data store.
   * 
   * @param entity An instance of the Entity that is deleted from the data store.
   */
  protected void validateAfterDelete(final T entity) {
    return;
  }

  @Override
  public String getQueryString(final FilterTerm term) {
    return term.toQueryString();
  }
  
  @Override
  public List<Object> getParamValues(final FilterTerm term) {
    
    String key = term.getKey();
    String value = term.getValue();
    
    List<Object> objectValues = new ArrayList<Object>();
    if (value == null)
      objectValues.add(value);
    else {
      Set<String> longFields = new HashSet<String>(Arrays.asList("ID", "CREATEDATE", "CREATEDBY", "LASTMODIFIEDDATE", "LASTMODIFIEDBY"));
      String[] values = value.split(",");
      
      for (String splitValue : values) {
        if (longFields.contains(key.toUpperCase())) {
          if (StringUtils.isNotBlank(splitValue))
            objectValues.add(Long.valueOf(splitValue.trim()));
          else
            objectValues.add(-1);
        } else {
          objectValues.add(splitValue);
        }
      }
    }
    return objectValues;
  }
  
  /**
   * Maps the input search criteria to a JDO query object
   * 
   * @param searchCriteria An instance of the Search Criteria object.
   * @param Map of 1 item where key is the query and the value is a parameters for the query.
   */
  protected Map<String, Map<Object, Object>> buildFilter(final SearchCriteria searchCriteria) {
    FilterExpression queryFilter = searchCriteria.getFilter();
    Map<String, Map<Object, Object>> filterMap = null;
    String filter = null;
    
    if (queryFilter != null) {
      filter = queryFilter.toQueryString(this);
      
      if (filter.length() > 0) {
        Map<Object, Object> queryParams = queryFilter.getQueryParams(this);
        filterMap = new HashMap<String, Map<Object, Object>>();
        filterMap.put(filter, queryParams);
      }
    }
    
    return filterMap;
  }
  
  protected Map<String, Object> getFilterParamMap(final SearchCriteria searchCriteria) {
    Map<String, Object> response = null;
    Map<String, Map<Object, Object>> filterMap = buildFilter(searchCriteria);
    
    if (filterMap != null && !filterMap.isEmpty()) {
      Iterator<String> keys = filterMap.keySet().iterator();
      String filter = keys.next();
      Map<Object, Object> paramMap = filterMap.get(filter);
      
      response = new HashMap<String, Object>();
      response.put("filter", filter);
      response.put("paramMap", paramMap);
    }
    
    return response;
  }
}