package com.jiggy.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.service.AbstractDBService;

/**
 * A concrete implementation of the UserCredentialsService.
 * 
 * @author jmalkan
 */
@Service("userCredentialsService")
public class UserCredentialsServiceImpl extends AbstractDBService<UserCredentials> implements UserCredentialsService {
  /**
   * Creates a new instance of com.jiggy.security.UserCredentialsServiceImpl.java and Performs Initialization
   *
   * @param userCredentialsDAO The UserCredentialsDAO Data Access Object this service interacts with.
   */
  @Autowired
  public UserCredentialsServiceImpl(final UserCredentialsDAO userCredentialsDAO) {
    super(userCredentialsDAO);
  }

  @Override
  protected void validateBeforeInsert(final UserCredentials entity) {
    return;	
  }

  @Override
  protected void validateBeforeUpdate(final UserCredentials entity) {
    return;	
  }

  @Override
  protected void validateBeforeDelete(final UserCredentials entity) {
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