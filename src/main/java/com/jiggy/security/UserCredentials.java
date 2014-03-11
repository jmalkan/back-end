package com.jiggy.security;

import com.jiggy.base.entity.AbstractEntity;

/**
 * An entity to represent User Credentials.
 * 
 * @author jmalkan
 */
public class UserCredentials extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  
  private String password;
  private Boolean changePassword = Boolean.FALSE;
  
  private User user;

  
  /**
   * Creates a new instance of com.jiggy.securit.UserCredentials.java and Performs Initialization
   */
  public UserCredentials() {
    super();
  }

  
  /**
   * Creates a new instance of com.jiggy.securit.UserCredentials.java and Performs Initialization
   * 
   * @param password The password.
   * @param changePassword The changePassword indicator.
   * @param user The user object.
   */
  public UserCredentials(final String password, final Boolean changePassword, final User user) {
    super();
    
    this.password = password;
    this.changePassword = changePassword;
    this.user = user;
  }

  public String getPassword() {
      return password;
  }
  
  public void setPassword(final String password) {
      this.password = password;
  }
  
  public Boolean getChangePassword() {
      return this.changePassword;
  }
  
  public void setChangePassword(final Boolean changePassword) {
      this.changePassword = changePassword;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }
}