package com.jiggy.base.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jiggy.controller.TodoController;
import com.jiggy.todo.Todo;
import com.jiggy.todo.TodoService;

public class AbstractControllerTest {
  Long id = Long.valueOf(1);
  @Mock private TodoService todoService;
  private TodoController todoController;
  private TodoController spyTodoController;
  
  @BeforeTest(alwaysRun = true)
  public void beforeTest() {
    MockitoAnnotations.initMocks(this);
    this.todoController = new TodoController(this.todoService);
    this.spyTodoController = spy(this.todoController);
  }
  
  @AfterTest
  public void afterMethod() {
    reset(this.todoService);
  }
  
  @Test
  public void getService() {
    assertNotNull(this.todoController.getService());
    assertEquals(this.todoController.getService(), this.todoService);
  }
  
  @Test
  public void findById() {
    this.spyTodoController.findById(id);
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeFindById(id);
    inOrder.verify(this.spyTodoController).implementFindById(id);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).findById(id);
    inOrder.verify(this.spyTodoController).afterFindById(id, null);
  }
  
  @Test
  public void beforeFindById() {
    this.spyTodoController.beforeFindById(id);
  }
  
//  @Test
//  public void implementFindById() {
//    this.spyTodoController.implementFindById(id);
//    
//    verify(this.spyTodoController).getService();
//    verify(this.todoService).findById(id);
//  }
  
  @Test
  public void afterFindById() {
    this.spyTodoController.afterFindById(id, null);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void findAll() {
    this.spyTodoController.findAll();
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeFindAll();
    inOrder.verify(this.spyTodoController).implementFindAll();
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).findAll();
    inOrder.verify(this.spyTodoController).afterFindAll(anyList());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void find() {
//  SearchCriteria searchCriteria = mock(SearchCriteria.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    this.spyTodoController.find(request);

//  when(this.spyTodoController.buildSearchCriteria(request)).thenReturn(searchCriteria);
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeFind(request);
    inOrder.verify(this.spyTodoController).implementFind(request);
//    inOrder.verify(this.spyTodoController).buildSearchCriteria(request);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).find(searchCriteria);
    inOrder.verify(this.spyTodoController).afterFind(anyList());
  }
  
  @Test
  public void findOne() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    this.spyTodoController.findOne(request);
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeFindOne(request);
    inOrder.verify(this.spyTodoController).implementFindOne(request);
//    inOrder.verify(this.spyTodoController).buildSearchCriteria(request);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).find(any(SearchCriteria.class));
    inOrder.verify(this.spyTodoController).afterFindOne(any(Todo.class));
  }
  
  @Test
  public void getRowCount() {
    this.spyTodoController.getRowCount();
    
    verify(this.spyTodoController).implementRowCount();
  }
  
  @Test
  public void getRowCountWithSearchCriteria() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    this.spyTodoController.getRowCount(request);
    
    verify(this.spyTodoController).implementRowCount(request);
  }
  
  @Test
  public void create() {
    Todo todo = any(Todo.class);
    this.spyTodoController.create(todo);
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeCreate(todo);
    inOrder.verify(this.spyTodoController).implementCreate(todo);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).insert(todo);
    inOrder.verify(this.spyTodoController).afterCreate(todo);
  }
  
  @Test
  public void modify() {
    Todo todo = new Todo();
    
    todo.setId(id);
    
    this.spyTodoController.modify(id, todo);
    
    InOrder inOrder = inOrder(this.spyTodoController);
    
    inOrder.verify(this.spyTodoController).beforeModify(id, todo);
    inOrder.verify(this.spyTodoController).implementModify(id, todo);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).update(todo);
    inOrder.verify(this.spyTodoController).afterModify(any(Todo.class));
  }
  /*
  @Test
  public void remove() {
    Todo todo = new Todo();
    
    todo.setId(id);
    
    this.spyTodoController.remove(id, todo);
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeRemove(id, todo);
    inOrder.verify(this.spyTodoController).implementRemove(id, todo);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).delete(todo);
    inOrder.verify(this.spyTodoController).afterRemove(id, todo);
  }
  */
  @Test
  public void removeById() {
    this.spyTodoController.remove(id);
    
    InOrder inOrder = inOrder(this.spyTodoController, this.todoService);
    
    inOrder.verify(this.spyTodoController).beforeRemove(id);
    inOrder.verify(this.spyTodoController).implementRemove(id);
//    inOrder.verify(this.spyTodoController).getService();
//    inOrder.verify(this.todoService).delete(id);
    inOrder.verify(this.spyTodoController).afterRemove(id);
  }
}
