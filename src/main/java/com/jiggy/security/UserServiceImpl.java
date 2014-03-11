package com.jiggy.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.service.AbstractDBService;

/**
 * A concrete implementation of the UserServiceImpl.
 * 
 * @author jmalkan
 */
@Service(value = "userService")
public class UserServiceImpl extends AbstractDBService<User> implements UserService {
  /**
   * Creates a new instance of com.jiggy.security.UserServiceImpl.java and Performs Initialization
   *
   * @param userDAO The UserCredentialsDAO Data Access Object this service interacts with.
   */
  @Autowired
  public UserServiceImpl(final UserDAO userDAO) {
    super(userDAO);
  }

  @Override
  protected void validateBeforeInsert(final User entity) {
    return;	
  }

  @Override
  protected void validateBeforeUpdate(final User entity) {
    return;	
  }

  @Override
  protected void validateBeforeDelete(final User entity) {
    return;	
  }

  @Override
  protected void validateBeforeCreate(final List<? extends Entity> entities) {
    return;	
  }

  @Override
  protected void validateBeforeModify(final List<? extends Entity> entities) {
    return;	
  }
  
  @Override
  protected void validateBeforeRemove(final List<? extends Entity> entities) {
    return;	
  }
}