package com.jiggy.base.web;

import com.jiggy.base.entity.Entity;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.representation.Form;

/**
 * This is the abstract base class for all the transformation classes used by Resources. Provides base interface and partial implementation for
 * transforming 1. Jersey Form into Entity that could be consumed by services. 2. Entity into JSON object to be returned to client.
 * 
 * @param <T> The core Entity object instance this class transforms.
 * 
 * @author jmalkan
 */
public abstract class AbstractTransformer<T extends Entity> implements Transformer<T> {
  @Override
  @SuppressWarnings("unchecked")
  public JSONArray entitiesToJSONArray(final List<? extends Entity> entities) throws JSONException {
    JSONArray jsonArray = new JSONArray();
    
    for (Entity entity : entities) {
      jsonArray.put(entityToJSON((T) entity));
    }
    
    return jsonArray;
  }
  
  @Override
  public Object formToEntityId(final Form form) {
    if (form != null && form.getFirst(getEntityIdColumnName()) != null) {
      return form.getFirst(getEntityIdColumnName(), Long.class);
    }
    
    return null;
  }
  
  protected String getEntityIdColumnName() {
    return Entity.ID_COLUMN_NAME;
  }
  
  protected boolean isIdPresent(final Form form) {
    return null != this.formToEntityId(form);
  }
}