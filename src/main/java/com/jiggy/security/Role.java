package com.jiggy.security;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jiggy.base.entity.AbstractEntity;

/**
 * An entity to represent Role.
 * 
 * @author jmalkan
 */
public class Role extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  
  private Set<Permission> permissions;
  
  /**
   * Creates a new instance of com.jiggy.security.Role.java and Performs Initialization
   */
  public Role() {
    super();
  }
  
  // /**
  // * Creates a new instance of com.jiggy.security.Role.java and Performs Initialization
  // *
  // * @param name The Role Name
  // * @param permissions The permissions assigned to the role
  // */
  // public Role(String name, Set<Permission> permissions) {
  // super();
  //
  // this.name = name;
  // this.permissions = permissions;
  // }
  
  public String getName() {
    return name;
  }
  
  public void setName(final String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(final String description) {
    this.description = description;
  }
  
  public Set<Permission> getPermissions() {
    return permissions;
  }
  
  public void setPermissions(final Set<Permission> permissions) {
    this.permissions = permissions;
  }
  
  /**
   * The filter for the given resource and operation combination.
   * 
   * @param resourse The name of the resource.
   * @param operation The name of the operation.
   * @return If found, The filter for the given resource and operation combination, else null.
   */
  public String getPermissionFilter(final String resourse, final String operation) {
    Permission permission = getPermission(resourse, operation);
    return (permission == null ? null : permission.getFilter());
  }
  
  /**
   * The filter for the given resource and operation combination.
   * 
   * @param resourse The name of the resource.
   * @param operation The name of the operation.
   * @return True, If a Permission found for the given resource and operation combination, else False.
   */
  public boolean isException(final String resourse, final String operation) {
    return (getPermission(resourse, operation) == null ? false : true);
  }
  
  /**
   * The Permission that matches the given resource and operation combination.
   * 
   * @param resourse The name of the resource.
   * @param operation The name of the operation.
   * @return If found, The Permission for the given resource and operation combination, else null.
   */
  public Permission getPermission(final String resourse, final String operation) {
    if (this.permissions != null && !this.permissions.isEmpty() && !StringUtils.isBlank(resourse) && !StringUtils.isBlank(operation)) {
      for (Permission permission : this.permissions) {
        if (resourse.equalsIgnoreCase(permission.getResource()) && operation.equalsIgnoreCase(permission.getOperation())) return permission;
      }
    }
    
    return null;
  }
}