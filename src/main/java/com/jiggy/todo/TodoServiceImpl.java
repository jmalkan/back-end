package com.jiggy.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiggy.base.entity.Entity;
import com.jiggy.base.service.AbstractDBService;

/**
 * A concrete implementation of the TodoService.
 * 
 * @author jmalkan
 */
@Service("todoService")
public class TodoServiceImpl extends AbstractDBService<Todo> implements TodoService {
  /**
   * Creates a new instance of com.jiggy.todo.TodoServiceImpl.java and Performs Initialization.
   * 
   * @param todoDAO The Todo Data Access Object this service interacts with.
   */
  @Autowired
  public TodoServiceImpl(final TodoDAO todoDAO) {
    super(todoDAO);
  }

  @Override
  protected void validateBeforeInsert(final Todo entity) {
    return;
  }

  @Override
  protected void validateBeforeUpdate(final Todo entity) {
    return;
  }

  @Override
  protected void validateBeforeDelete(final Todo entity) {
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