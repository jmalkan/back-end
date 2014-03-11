package com.jiggy.base.web;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.searchengine.DefaultSearchCriteria;
import com.jiggy.base.searchengine.SearchCriteria;
import com.jiggy.base.searchengine.FilterExpression.FilterTerm;
import com.jiggy.base.service.DBService;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * AbstractResource.java
 * 
 * <p>
 * Web-Service layer facade base class with common functionality and extension points for the descendants. Implements DBResource interface and
 * provides base implementation for the methods that could be extended by the descendant class. It calls Before and After abstract/empty methods for
 * additional logic during create, modify, and delete which descendants needs to provide implementation. This is not where business or data validation
 * logic should be implemented.
 * 
 * @author jmalkan
 * 
 * @param <T> The Entity object used for transformation.
 * 
 * @see com.jiggy.base.web.DBResource
 */
public abstract class AbstractResource<T extends Entity> extends BaseResource implements DBResource<T> {
  private final static Logger logger = LoggerFactory.getLogger(AbstractResource.class);
  
  private DBService<T> service = null;
  private Transformer<T> transformer = null;
  
  /**
   * Creates a new instance of com.jiggy.base.web.AbstractResource.java and Performs Initialization
   * 
   * @param service The Service that DBResource interacts with.
   */
  public AbstractResource(final DBService<T> service) {
    super();
    this.service = service;
  }
  
  /**
   * Creates a new instance of com.jiggy.base.web.AbstractResource.java and Performs Initialization
   * 
   * @param service The Service that DBResource interacts with.
   * @param transformer The transformer.
   */
  public AbstractResource(final DBService<T> service, final Transformer<T> transformer) {
    super();
    this.service = service;
    this.transformer = transformer;
  }
  
  /**
   * Getter of the property <tt>service</tt>
   * 
   * @return the service
   */
  public DBService<T> getService() {
    return this.service;
  }
  
  /**
   * Getter of the property <tt>transformer</tt>
   * 
   * @return the transformer
   */
  public Transformer<T> getTransformer() {
    return this.transformer;
  }
  
  @Override
  public JSONObject findById(final Long id) throws JSONException {
    this.beforeFindById(id);
    JSONObject foundEntity = this.implementFindById(id);
    this.afterFindById(id, foundEntity);
    
    return foundEntity;
  }
  
  @Override
  public JSONArray findAll() throws JSONException {
    this.beforeFindAll();
    JSONArray foundEntities = this.implementFindAll();
    this.afterFindAll(foundEntities);
    
    return foundEntities;
  }
  
  @Override
  public JSONObject find() throws JSONException {
    this.beforeFind();
    JSONObject jsonObject = this.implementFind();
    this.afterFind(jsonObject);
    
    return jsonObject;
  }
  
  @Override
  public JSONObject create(final Form form) throws JSONException {
    this.beforeCreate(form);
    JSONObject jsonObject = this.implementCreate(form);
    this.afterCreate(jsonObject);
    
    return jsonObject;
  }
  
  @Override
  public JSONObject modify(final Long id, final Form form) throws JSONException {
    this.beforeModify(id, form);
    JSONObject jsonObject = this.implementModify(id, form);
    this.afterModify(jsonObject);
    return jsonObject;
  }
  
  @Override
  public void remove(Long id) {
    this.beforeRemove(id);
    this.implementRemove(id);
    this.afterRemove(id);
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   * 
   * @param id The id of the Domain/Entity that is about to be accessed.
   */
  protected void beforeFindById(final Long id) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id The id for the entity to be searched for
   * @return If found, an instance of the JSONObject of the found entity object, else null.
   * @throws JSONException If the transformer fails
   */
  protected JSONObject implementFindById(final Long id) throws JSONException {
    T entity = this.getService().findById(id);
    return this.getTransformer().entityToJSON(entity);
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param id The id received from the client.
   * @param jsonObject found.
   */
  protected void afterFindById(final Long id, final JSONObject jsonObject) {
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
   * @return If found, a list of the JSONObject of the found entity object, else null.
   * @throws JSONException
   */
  protected JSONArray implementFindAll() throws JSONException {
    List<T> entities = this.getService().findAll();
    return this.getTransformer().entitiesToJSONArray(entities);
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param jsonNArray contains list of jsonObjects found.
   */
  protected void afterFindAll(final JSONArray jsonNArray) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic, typically a security access check before entity is accessed.
   * 
   * @param form capturing user input.
   */
  protected void beforeFind() {
    return;
  }
  
  /**
   * The extending class may over-ride the implementation.
   * 
   * @return If found, a list of the JSONObject of the found entity object, else null.
   * @throws JSONException any JSONException during transformation.
   */
  protected JSONObject implementFind() throws JSONException {
    JSONObject jsonObject = new JSONObject();
    SearchCriteria searchCriteria = this.buildSearchCriteria();
    
    if (searchCriteria != null) {
      List<T> entities = this.getService().find(searchCriteria);
      JSONArray jsonArray = this.getTransformer().entitiesToJSONArray(entities);
      
      jsonObject.put("count", (searchCriteria.getRowCount() < jsonArray.length() ? jsonArray.length() : searchCriteria.getRowCount()));
      jsonObject.put("data", jsonArray);
    }
    
    return jsonObject;
  }
  
  /**
   * Builds Search Criteria from the Query param.
   * 
   * @return An instance of the Search Criteria.
   */
  protected SearchCriteria buildSearchCriteria() {
    MultivaluedMap<String, String> query = new MultivaluedMapImpl(this.getUriInfo().getQueryParameters());
    Iterator<String> keys = query.keySet().iterator();
    DefaultSearchCriteria defaultSearchCriteria = null;
    
    while (keys.hasNext()) {
      if (defaultSearchCriteria == null) defaultSearchCriteria = new DefaultSearchCriteria();
      
      String key = keys.next();
      List<String> vals = query.get(key);
      
      if ("sort".equalsIgnoreCase(key))
        defaultSearchCriteria.setSortBy(vals.get(0));
      else if ("offset".equalsIgnoreCase(key))
        defaultSearchCriteria.setOffset(Integer.parseInt(vals.get(0)));
      else if ("limit".equalsIgnoreCase(key))
        defaultSearchCriteria.setLimit(Integer.parseInt(vals.get(0)));
      else
        defaultSearchCriteria.addFilter(new FilterTerm(key, vals.get(0)));
    }
    
    return defaultSearchCriteria;
  }
  
  /**
   * Implements business (non-data) validation logic after finding an entity by id.
   * 
   * @param entities capturing user input.
   * 
   * @param list of jsonObjects found.
   */
  protected void afterFind(final JSONObject entities) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is created in the data store.
   * 
   * @param form capturing user input.
   */
  protected void beforeCreate(final Form form) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param form capturing user input.
   * @throws JSONException
   */
  protected JSONObject implementCreate(final Form form) throws JSONException {
    logger.debug("Check if this is an insert request");
    
    T entitity = this.getTransformer().formToEntity(form);
    
    if (entitity != null) return this.getTransformer().entityToJSON(this.getService().insert(entitity));
    
    logger.debug("Check if this is an create request");
    
    List<? extends Entity> entities = this.getTransformer().formToEntities(form);
    
    return this.getTransformer().entitiesToJSON(this.getService().create(entities));
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is created in the data store.
   * 
   * @param jsonObject The JSONObject capturing newly created entities.
   */
  protected void afterCreate(final JSONObject jsonObject) {
    return;
  }
  
  /**
   * Implements non-business/non-data validation logic before entity is updated in the data store.
   * 
   * @param id The id of the entity to be updated.
   * @param form capturing user input.
   */
  protected void beforeModify(final Long id, final Form form) {
    return;
  }
  
  /**
   * The extending class may provide the implementation.
   * 
   * @param id The id of the entity to be updated.
   * @param form capturing user input.
   * @throws JSONException If the transformer fails
   */
  protected JSONObject implementModify(final Long id, final Form form) throws JSONException {
    logger.debug("Check if this is an update request");
    
    T entity = find(id, form);
    entity = this.getTransformer().formToEntity(form, entity);
    
    if (entity != null) return this.getTransformer().entityToJSON(this.getService().update(entity));
    logger.debug("Check if this is an modify request");
    
    List<? extends Entity> entities = this.getTransformer().formToEntities(form);
    
    return this.getTransformer().entitiesToJSON(this.getService().modify(entities));
  }
  
  /**
   * Finds an entity from the form input.
   * 
   * @param form capturing user input.
   * @return an Entity if found else null.
   */
  protected T find(final Long id, final Form form) {
    T entity = null;
    
    if (id != null) entity = this.getService().findById(id);
    
    if (entity == null) {
      Object entityId = this.getTransformer().formToEntityId(form);
      
      if (entityId instanceof SearchCriteria) entity = this.getService().findOne((SearchCriteria) entityId);
    }
    
    return entity;
  }
  
  /**
   * Implements non-business/non-data validation logic after entity is updated in the data store.
   * 
   * @param jsonObject The JSONObject capturing modified created entity.
   */
  protected void afterModify(final JSONObject jsonObject) {
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
}