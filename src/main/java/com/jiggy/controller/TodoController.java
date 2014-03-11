package com.jiggy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiggy.base.controller.AbstractController;
import com.jiggy.todo.Todo;
import com.jiggy.todo.TodoService;

/**
 * Handles requests for the application todos request.
 * 
 * @author jmalkan
 */
@Controller
@RequestMapping("todos")
public class TodoController extends AbstractController<Todo> {
  /**
   * Creates a new instance of com.jiggy.controller.TodosController.java and Performs Initialization
   */
  @Autowired
  public TodoController(final TodoService todoService) {
    super(todoService);
  }
}