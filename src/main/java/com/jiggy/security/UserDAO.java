package com.jiggy.security;

import com.jiggy.base.dao.DBDAO;

/**
 * Data access interface for User.
 * 
 * @author jmalkan
 */
public interface UserDAO extends DBDAO<User> {
  //Define non-standard CRUD methods.
}