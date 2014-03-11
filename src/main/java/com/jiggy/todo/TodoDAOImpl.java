package com.jiggy.todo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.jiggy.base.dao.AbstractMongoDBDAO;
import com.jiggy.base.exception.Error;
import com.jiggy.base.exception.ValidationException;
import com.jiggy.base.searchengine.SearchCriteria;

/**
 * Data access implementation for Todo.
 * 
 * Created on Sept 1, 2012
 * 
 * @author jmalkan
 * @version $Revision$
 */
@Repository("todoDAO")
public class TodoDAOImpl extends AbstractMongoDBDAO<Todo> implements TodoDAO {
  private List<Todo> inMemoryTodoDB;
  
  /**
   * Creates a new instance of com.jiggy.todo.TodoDAOImpl.java and Performs Initialization
   * 
   * @param sessionFactory The Hibernate's sessionFactory Object this dao interacts with.
   */
  @Autowired
  public TodoDAOImpl(final MongoOperations mongoOperations) {
    super(mongoOperations);
    
    inMemoryTodoDB = new ArrayList<Todo>();
    
    inMemoryTodoDB.add(new Todo(new Long(1), "wake up"));
    inMemoryTodoDB.add(new Todo(new Long(2), "do dishes"));
    inMemoryTodoDB.add(new Todo(new Long(3), "take out trash"));
  }

  @Override
  protected List<Todo> implementFindAll() {
    logger.info("implementFindAll");
    
    return inMemoryTodoDB;
  }

  @Override
  protected Todo implementFindById(Long id) {
    logger.info("implementFindById");
    
    return inMemoryTodoDB.get(id.intValue() - 1);
  }

  @Override
  protected List<Todo> implementFind(SearchCriteria searchCriteria) {
    logger.info("implementFind");
    
    return inMemoryTodoDB.subList(0, 1);
  }

  @Override
  protected Todo implementFindOne(SearchCriteria searchCriteria) {
    logger.info("implementFindOne");
    Error error = Error.ERROR_REQUIRED;
    
    error.formatMessage("name");
    error.setAttributeName("name");
    
    throw new ValidationException(error);
  }

  @Override
  protected Todo implementInsert(Todo entity) {
    logger.info("implementInsert");
    entity.setId(new Long(inMemoryTodoDB.size() + 1));
    return this.inMemoryTodoDB.set(entity.getId().intValue(), entity);
  }

  @Override
  protected Todo implementUpdate(Todo modifiedEntity) {
    logger.info("implementUpdate");
    if (modifiedEntity != null) {
      int index = modifiedEntity.getId().intValue() -1;
      Todo todo = inMemoryTodoDB.get(index);
      
      if (todo != null)
        inMemoryTodoDB.set(index, modifiedEntity);
    }
    
    return modifiedEntity;
  }

  @Override
  protected void implementDelete(Todo entity) {
    logger.info("implementDelete");
    this.inMemoryTodoDB.remove(entity.getId().intValue() - 1);
  }
}