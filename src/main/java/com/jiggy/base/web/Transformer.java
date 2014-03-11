package com.jiggy.base.web;

import com.jiggy.base.entity.Entity;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.representation.Form;

/**
 * Transformer interface.
 * 
 * @author jmalkan
 * 
 * @param <T> The Entity object used for transformation.
 */
public interface Transformer<T extends Entity> {
  /**
   * This method transforms Jersey Form to an Entity that could be passed to the Service Layer.
   * 
   * @param form The Jersey form containing user input.
   * @return The list containing Entity <T>.
   */
  T formToEntity(final Form form);
  
  /**
   * This method transforms Jersey Form to an existing Entity that could be passed to the Service Layer.
   * 
   * @param form The Jersey form containing user input.
   * @param entity An entity from the data store that need to be populated with the data from the form.
   * @return The list containing Entity <T>.
   */
  T formToEntity(final Form form, final T entity);
  
  /**
   * This methods return the entity Id read from the form. This is used to read the object of the data store before making changes.
   * 
   * @param form The Jersey form containing user input.
   * 
   * @return An entity id. This could be a Long for most cases. In complex cases would be an instance of the Search Criteria.
   */
  Object formToEntityId(final Form form);
  
  /**
   * This method transforms Jersey Form with list of entities of different type that could be passed to the Service Layer.
   * 
   * @param form The Jersey form containing user input.
   * @return The list containing Entity possibly of different types..
   */
  List<? extends Entity> formToEntities(final Form form);
  
  /**
   * This method transforms entity to JSONObject that could be passed to the Client Layer.
   * 
   * @param entity The Entity <T>.
   * @return The list of transformed JSONObjects
   * @throws JSONException Thrown when the transformation fails.
   */
  JSONObject entityToJSON(final T entity) throws JSONException;
  
  /**
   * This method transforms list of entities of different type to JSONObject that could be passed to the Client Layer.
   * 
   * @param entities The list of Entities
   * @return The list containing Entity of different types. For example User, Org etc... This may have 5 Users, 5 Orgs.
   * @throws JSONException Thrown when the transformation fails.
   */
  JSONObject entitiesToJSON(final List<? extends Entity> entities) throws JSONException;
  
  /**
   * This method transforms list of entities of the same type to JSONArray that could be passed to the Client Layer.
   * 
   * @param entities The list of Entities
   * @return The list containing Entity of same or different types. For example 10 instances of the User or Org etc... This must not have instances of
   *         User and Org
   * @throws JSONException Thrown when the transformation fails.
   */
  JSONArray entitiesToJSONArray(final List<? extends Entity> entities) throws JSONException;
  
  /**
   * Provides a means for the EntityTransformerFactoryProvider to identify the template class for this interface. The provider inspects the return
   * type of this method to determine what the template class is. It **must** be implemented by all concrete implementations of this interface and
   * cannot be implemented by any abstract classes. No actual implementation need be provided for this method as the provider inspects the return type
   * of this method but never invokes it.
   * 
   * @return No need to implement this, but if you did an instance of the Entity that the Transformer applies too.
   */
  T createTransformableEntity();
}