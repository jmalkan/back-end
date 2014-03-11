package com.jiggy.security;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * UserProfile is simple POJO that maintains current user's state in the Shiro Session.
 * 
 * @author jmalkan
 */
public class UserProfile implements Serializable {
  private static final long serialVersionUID = 1L;
  private User user;
  private Role role;
  
  /**
   * Creates a new instance of com.jiggy.security.UserProfile.java and Performs Initialization
   * 
   * @param user The Users object containing Role/Permission.
   */
  public UserProfile(final User user) {
    super();
    this.user = user;
  }
  
  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(final Role role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}