package com.jiggy.security;

import java.io.Serializable;

/**
 * The User Principal object used for Authentication.
 * 
 * @author jmalkan
 */
public class UserPrincipal implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  
  
  /**
   * Creates a new instance of com.jiggy.security.UserPrincipal.java and Performs Initialization
   */
  public UserPrincipal(final Long id) {
    super();
    this.id = id;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(final Long id) {
    this.id = id;
  }
}