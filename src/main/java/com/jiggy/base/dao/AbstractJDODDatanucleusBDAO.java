package com.jiggy.base.dao;

import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.lang3.StringUtils;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.SearchCriteria;

/**
 * <p>
 * Data access layer base class for JDO Datanucleus.
 * 
 * @see AbstractDBDAO<T>
 * 
 * @author jmalkan
 */
public abstract class AbstractJDODDatanucleusBDAO<T extends Entity> extends AbstractDBDAO<T> {
  protected PersistenceManager persistenceManager;
  
  public AbstractJDODDatanucleusBDAO(final PersistenceManager persistenceManager) {
    super();
    this.persistenceManager = persistenceManager;
  }
  
  /**
   * Get method for persistenceManager
   * 
   * @return the instance of the persistenceManager
   */
  public PersistenceManager getPersistenceManager() {
    return persistenceManager;
  }
  
  /**
   * Implements logic to find an entity for the given id.
   * 
   * @param id The id of the entity being searched.
   * @return entity An instance of the Entity that is retrieved from the data store.
   */
  protected T implementFindById(final Long id) {
    return (T) this.getPersistenceManager().getObjectById(super.getPersistentClass(), id);
  }
  
  /**
   * Implements logic to find all the entities that matches the given search criteria.
   * 
   * @param searchCriteria The SearchCriteria object with user input
   * @return List of entity objects that matches the search criteria
   * 
   * @see SearchCriteria
   */
  @SuppressWarnings("unchecked")
  protected List<T> implementFind(final SearchCriteria searchCriteria) {
    List<T> result = null;
    
    if (searchCriteria != null) {
      String filter = null;
      Map<Object, Object> paramMap = null;
      Map<String, Object> filterParamMap = super.getFilterParamMap(searchCriteria);
      Query query = this.getPersistenceManager().newQuery(super.getPersistentClass());
      
      this.applySecurityFilter(searchCriteria);
      
      if (StringUtils.isNotBlank(searchCriteria.getQueryVariables()))
        query.declareVariables(searchCriteria.getQueryVariables());
      
      if (filterParamMap != null && !filterParamMap.isEmpty()) {
        filter = (String) filterParamMap.get("filter");
        paramMap = (Map<Object, Object>) filterParamMap.get("filterMap");
      }
      
      if (searchCriteria.isPaginationEnabled()) {
        logger.debug("Pagination is enabled, calculate range.");
        long rowCount = this.getRowcount(searchCriteria, filter, paramMap);
        
        if (searchCriteria.getOffset() > -1 && searchCriteria.getLimit() > 0 && rowCount > 0) {
          searchCriteria.setRowCount(rowCount);
          
          logger.debug("Calculate the Start Row Number.");
          long lowerBound = searchCriteria.getOffset() < rowCount ? searchCriteria.getOffset() : rowCount - searchCriteria.getLimit();
          
          logger.debug("Calculate the End Row Number.");
          long upperBound = lowerBound + searchCriteria.getLimit() <= rowCount ? lowerBound + searchCriteria.getLimit() : rowCount;
          logger.debug("Adding range to the Main Search query.");
          
          if (lowerBound != -1 && upperBound != -1 && lowerBound != upperBound)
            query.setRange(lowerBound, upperBound);
        }
      }
      
      logger.debug("Adding filters to the Main Search query.");
      if (filter != null)
        query.setFilter(filter);
      
      logger.debug("Adding soring to the Main Search query.");
      if (!StringUtils.isBlank(searchCriteria.getSortBy()))
        query.setOrdering(searchCriteria.getSortBy());
      
      logger.debug("Executing the Main Search Query={}  with Param={}", query.toString(), paramMap);
      result = (List<T>) query.executeWithMap(paramMap);
      
      if (searchCriteria.getRowCount() < 0 && result != null)
        searchCriteria.setRowCount(result.size());
    }
    
    return result;
  }

  @Override
  protected long implementRowCount(final SearchCriteria searchCriteria) {
    return this.getRowcount(searchCriteria, null, null);
  }
  
  
  @SuppressWarnings("unchecked")
  private long getRowcount(final SearchCriteria searchCriteria, String filter, Map<Object, Object> paramMap) {
    long rowCount = -1;
    Long rowCountResult;
    
    logger.debug("Build Row Count query.");
    Query rowCountQuery = this.getPersistenceManager().newQuery(this.getPersistenceManager().getExtent(super.getPersistentClass(), false), "true");
    
    rowCountQuery.setResult("count(this)");
    
    if (searchCriteria != null) {
      if (filter == null && paramMap == null) {
        Map<String, Object> filterParamMap = super.getFilterParamMap(searchCriteria);
        
        if (filterParamMap != null && !filterParamMap.isEmpty()) {
          filter = (String) filterParamMap.get("filter");
          paramMap = (Map<Object, Object>) filterParamMap.get("filterMap");
        }
      }
      
      logger.debug("Declaring Variables to Row Count query.");
      if (StringUtils.isNotBlank(searchCriteria.getQueryVariables()))
        rowCountQuery.declareVariables(searchCriteria.getQueryVariables());
      
      logger.debug("Adding filters to Row Count query.");
      if (filter != null)
        rowCountQuery.setFilter(filter);
      
      logger.debug("Executing the Row Count Query={}  with Param={}", rowCountQuery.toString(), paramMap);
      rowCountResult = (Long) rowCountQuery.executeWithMap(paramMap);
    } else
      rowCountResult = (Long) rowCountQuery.execute();
    
    if (rowCountResult != null)
      rowCount = rowCountResult.intValue();
    
    return rowCount;
  }
  
  /**
   * Implements logic to persist the entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * 
   * @return The Id of the newly created entity.
   */
  @Override
  protected T implementInsert(final T entity) {
    return this.getPersistenceManager().makePersistent(entity);
  }
  
  /**
   * Implements logic to persist the modified entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object.
   * @return The modified entity.
   */
  protected T implementUpdate(final T entity) {
    this.getPersistenceManager().setCopyOnAttach(false);
    return this.getPersistenceManager().makePersistent(entity);
  }
  
  /**
   * Implements logic to delete an persisted entity and its mapped associations to the underlying data store.
   * 
   * @param entity An instance of the entity object that needs to be deleted.
   */
  protected void implementDelete(final T entity) {
    this.getPersistenceManager().deletePersistent(entity);
  }
}