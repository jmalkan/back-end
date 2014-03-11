package com.jiggy.controller;

import static org.mockito.Mockito.reset;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jiggy.todo.TodoService;

public class TodoControllerTest {
  @Mock private TodoService todoService;
  @InjectMocks private TodoController todoController;
  
  @BeforeTest(alwaysRun = true)
  public void beforeTest() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterMethod() {
    reset(this.todoService);
  }
  
  @Test
  public void getService() {
    assertNotNull(this.todoController.getService());
    assertEquals(this.todoController.getService(), this.todoService);
  }
}
