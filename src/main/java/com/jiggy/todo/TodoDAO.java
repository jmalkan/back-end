package com.jiggy.todo;

import com.jiggy.base.dao.DBDAO;

/**
 * Data access interface for Todo.
 * 
 * @author jmalkan
 */
public interface TodoDAO extends DBDAO<Todo> {
  //Define non-standard CRUD methods.
}