package com.jiggy.base.web;

import com.jiggy.base.entity.Entity;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.representation.Form;

/**
 * DBResource.java A DBResource Interface.
 * 
 * @author jmalkan
 * 
 * @param <T> An Object that implements Entity interface.
 */
public interface DBResource<T extends Entity> extends Resource {
  /**
   * Finds the entity for the given Id
   * 
   * @param id The id for the entity to be searched for
   * @return If found, an instance of the JSONObject of the found entity object, else null.
   * @throws JSONException error while converting to JSON.
   */
  @GET
  @Path("/{id}/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  JSONObject findById(@PathParam("id") final Long id) throws JSONException;
  
  /**
   * Finds all the entity objects
   * 
   * @return If found, a list of the JSONObject of the found entity object, else null.
   * @throws JSONException error while converting to JSON.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  JSONArray findAll() throws JSONException;
  
  /**
   * Finds the entity for the given search criteria for the user input for sort, pagination, filter. This should come in as name=value pair. multiple
   * name=values be separated by '&'. Example http://localhost:8080/../find/name=CookBook&desc=CookBookDesc&sort=name&offset=1&limit=10.
   * 
   * @return A JSONObject with 2 elements &lt;b&gt;count&lt;/b&gt; and &lt;b&gt;data&lt;/b&gt;. &lt;b&gt;count:&lt;/b&gt; is the total number of rows
   *         the given filter provided to this operation after find?.... &lt;b&gt;data:&lt;/b&gt; If pagination attributes (offset & limit) are
   *         provided data only returns subset of total row count. Else count will match the data size. Example: {count: 10, data: [{ item1s data }, {
   *         item2s data }, ....]}
   * @throws JSONException error while converting to JSON.
   */
  @GET
  @Path("/find")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  JSONObject find() throws JSONException;
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param form capturing user input.
   * 
   * @return The JSONObject capturing newly created entities.
   * @throws JSONException error while converting to JSON.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  JSONObject create(final Form form) throws JSONException;
  
  /**
   * Runs business logic and persists the entity object and its mapped associations to the underlying data store.
   * 
   * @param id The id of the entity to be updated.
   * @param form user input.
   * 
   * @return The JSONObject capturing modified entities.
   * @throws JSONException error while converting to JSON.
   */
  @PUT
  @Path("/{id}/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  JSONObject modify(@PathParam("id") final Long id, final Form form) throws JSONException;
  
  /**
   * Runs business logic and deletes the entity from the data store. Cascades if configured
   * 
   * @param id the Id of the entity to be removed.
   */
  @DELETE
  @Path("/{id}/")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  void remove(@PathParam("id") final Long id);
}