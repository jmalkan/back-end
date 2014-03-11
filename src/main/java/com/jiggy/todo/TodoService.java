package com.jiggy.todo;

import com.jiggy.base.service.DBService;

/**
 * The Business Logic Interface for accessing Todo.
 * 
 * @author jmalkan
 */
public interface TodoService extends DBService<Todo> {
  //Define non-standard CRUD methods.
}