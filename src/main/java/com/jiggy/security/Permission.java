package com.jiggy.security;

import com.jiggy.base.entity.AbstractEntity;

/**
 * An entity to represent Permission.
 * 
 * @author jmalkan
 */
public class Permission extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  private String resource;
  private String operation;
  private String filter;
  
  
  /**
   * Creates a new instance of com.jiggy.security.Permission.java and Performs Initialization
   */
  public Permission() {
    super();
  }
  
//  /**
//   * Creates a new instance of com.jiggy.securit.Permission.java and Performs Initialization
//   * 
//   * @param resource The Resource Name.
//   * @param operation The Operation Name.
//   */
//  public Permission(String resource, String operation) {
//    super();
//    
//    this.resource = resource;
//    this.operation = operation;
//  }
  
  /**
   * Getter of the property <tt>resource</tt>
   * 
   * @return the resource
   */
  public String getResource() {
    return this.resource;
  }
  
  /**
   * Setter of the property <tt>resource</tt>
   * 
   * @param resource the resource to set
   */
  public void setResource(final String resource) {
    this.resource = resource;
  }
  
  /**
   * Getter of the property <tt>operation</tt>
   * 
   * @return the operation
   */
  public String getOperation() {
    return this.operation;
  }
  
  /**
   * Setter of the property <tt>operation</tt>
   * 
   * @param operation the operation to set
   */
  public void setOperation(final String operation) {
    this.operation = operation;
  }
  
  public String getFilter() {
    return filter;
  }

  public void setFilter(final String filter) {
    this.filter = filter;
  }

  /**
   * Convenient method to provide the entire permission
   * 
   * @return The complete permission
   */
  public String getPermissionValue() {
    return this.getResource() + ":" + this.getOperation();
  }
}