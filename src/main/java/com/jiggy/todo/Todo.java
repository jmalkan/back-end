package com.jiggy.todo;

import com.jiggy.base.entity.AbstractEntity;

/**
 * An entity to represent Todo.
 * 
 * @author jmalkan
 */
public class Todo extends AbstractEntity {
  private static final long serialVersionUID = 1L;
  private String name;
  
  

  /**
   * Creates a new instance of com.jiggy.todo.Todo.java and Performs Initialization.
   */
  public Todo() {
    super();
  }

  /**
   * Creates a new instance of com.jiggy.todo.Todo.java and Performs Initialization.
   * 
   * @param id The task id.
   * @param name The task Name.
   */
  public Todo(final Long id, final String name) {
    super(id);

    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}